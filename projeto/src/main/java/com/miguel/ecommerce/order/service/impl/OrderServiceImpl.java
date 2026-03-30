package com.miguel.ecommerce.order.service.impl;

import com.miguel.ecommerce.address.entity.Address;
import com.miguel.ecommerce.address.repository.AddressRepository;
import com.miguel.ecommerce.cart.entity.Cart;
import com.miguel.ecommerce.cart.entity.CartItem;
import com.miguel.ecommerce.cart.repository.CartRepository;
import com.miguel.ecommerce.cart.service.CartService;
import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.order.dto.OrderItemResponse;
import com.miguel.ecommerce.order.dto.OrderRequest;
import com.miguel.ecommerce.order.dto.OrderResponse;
import com.miguel.ecommerce.order.entity.Order;
import com.miguel.ecommerce.order.entity.OrderItem;
import com.miguel.ecommerce.order.entity.OrderStatus;
import com.miguel.ecommerce.order.mapper.OrderMapper;
import com.miguel.ecommerce.order.repository.OrderRepository;
import com.miguel.ecommerce.order.service.OrderService;
import com.miguel.ecommerce.stock.entity.Stock;
import com.miguel.ecommerce.stock.repository.StockRepository;
import com.miguel.ecommerce.user.entity.User;
import com.miguel.ecommerce.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal SHIPPING_COST = new BigDecimal("5.00");

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;


    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found", userId)
        );

        Address address = addressRepository.findById(orderRequest.addressId()).orElseThrow(
                () -> new ResourceNotFoundException("Address not found", orderRequest.addressId())
        );
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new BusinessException("Cart not found"));

        if (cart.getCartItems().isEmpty()){
            throw new BusinessException("Cart is empty");
        }
        for (CartItem cartItem : cart.getCartItems()) {
            Stock stock = stockRepository.findById(cartItem.getProduct().getId()).
                    orElseThrow(()-> new ResourceNotFoundException("Stock", cartItem.getProduct().getId()));
            if (stock.getQuantity() < cartItem.getQuantity()) {
                throw new BusinessException("Insufficient stock");
            }
        }
        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .shippingCost(SHIPPING_COST)
                .build();

        List<OrderItem> ordemItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Stock stock = stockRepository.findByProductId(cartItem.getProduct().getId()).get();
                    stock.setQuantity(stock.getQuantity() - cartItem.getQuantity());
                    stockRepository.save(stock);

                    return OrderItem.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .build();
                }).toList();

        BigDecimal subTotal = ordemItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(subTotal.add(SHIPPING_COST));
        order.setItems(ordemItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
        return buildOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(this::buildOrderResponse)
                .toList();    }

    @Override
    public OrderResponse findById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return buildOrderResponse(order);    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException("Order cannot be cancelled");
        }

        order.getItems().forEach(item -> {
            Stock stock = stockRepository.findByProductId(item.getProduct().getId()).get();
            stock.setQuantity(stock.getQuantity() + item.getQuantity());
            stockRepository.save(stock);
        });

        order.setStatus(OrderStatus.CANCELLED);
        return buildOrderResponse(orderRepository.save(order));
    }


    private OrderResponse buildOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getAddress().getId(),
                order.getStatus(),
                order.getTotal(),
                order.getShippingCost(),
                orderMapper.toOrderItemResponseList(order.getItems()),
                order.getCreatedAt()
        );
    }
}
