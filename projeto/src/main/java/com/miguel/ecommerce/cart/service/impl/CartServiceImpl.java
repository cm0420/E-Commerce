package com.miguel.ecommerce.cart.service.impl;

import com.miguel.ecommerce.cart.dto.CartItemRequest;
import com.miguel.ecommerce.cart.dto.CartItemResponse;
import com.miguel.ecommerce.cart.dto.CartResponse;
import com.miguel.ecommerce.cart.entity.Cart;
import com.miguel.ecommerce.cart.entity.CartItem;
import com.miguel.ecommerce.cart.mapper.CartMapper;
import com.miguel.ecommerce.cart.repository.CartItemRepository;
import com.miguel.ecommerce.cart.repository.CartRepository;
import com.miguel.ecommerce.cart.service.CartService;
import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.product.entity.Product;
import com.miguel.ecommerce.product.repository.ProductRepository;
import com.miguel.ecommerce.stock.entity.Stock;
import com.miguel.ecommerce.stock.repository.StockRepository;
import com.miguel.ecommerce.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final CartMapper cartMapper;

    @Override
    public Cart getOrCreatCart(Long userId) {
        return cartRepository.findByUserId(userId).
                orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(new User());
                    newCart.getUser().setId(userId);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public CartResponse buildCartResponse(Cart cart) {
        List<CartItemResponse> cartItems = cart.getCartItems().stream()
                .map(item -> {
                    CartItemResponse cartItemResponse = cartMapper.toCartItemResponse(item);
                    BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new CartItemResponse(
                            cartItemResponse.id(),
                            cartItemResponse.productId(),
                            cartItemResponse.productName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            subtotal
                    );
                }).toList();
        BigDecimal total = cartItems.stream().
                map(CartItemResponse::subTotal).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(cart.getId(), cart.getUser().getId(), cartItems, total);
    }

    @Override
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreatCart(userId);
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest request) {
        Cart cart = getOrCreatCart(userId);

        Product product = productRepository.findById(request.productId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found", request.productId()));

        if (!product.getIsActive()) {
            throw new BusinessException("Product is not available");
        }
        Stock stock = stockRepository.findByProductId(request.productId()).orElseThrow(
                () -> new ResourceNotFoundException("Stock not found", request.productId())
        );
        if (stock.getQuantity() < request.quantity()) {
            throw new BusinessException("Insufficient Stock");
        }
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId());
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
            cartItemRepository.save(cartItem);
        }else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(request.quantity());
            newCartItem.setUnitPrice(product.getPrice());
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }
        Cart updatedCart = cartRepository.save(cart);
        return buildCartResponse(updatedCart);

    }

    @Override
    @Transactional
    public CartResponse removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException("Cart", userId));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId).
                orElseThrow(()-> new BusinessException("Product not found in cart"));
        cartItemRepository.delete(cartItem);
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long productId, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart", userId)
        );
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new BusinessException("Product not found in cart"));

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", productId));

        if (stock.getQuantity() < request.quantity()) {
            throw new BusinessException("Insufficient stock");
        }

        item.setQuantity(request.quantity());
        cartItemRepository.save(item);

        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        return buildCartResponse(updatedCart);

    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", userId));
        cartItemRepository.deleteByCartId(cart.getId());
    }
}
