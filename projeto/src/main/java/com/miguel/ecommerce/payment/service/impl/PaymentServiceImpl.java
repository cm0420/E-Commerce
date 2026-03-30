package com.miguel.ecommerce.payment.service.impl;

import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.order.entity.Order;
import com.miguel.ecommerce.order.entity.OrderStatus;
import com.miguel.ecommerce.order.repository.OrderRepository;
import com.miguel.ecommerce.payment.Strategy.PaymentResult;
import com.miguel.ecommerce.payment.Strategy.PaymentStrategy;
import com.miguel.ecommerce.payment.Strategy.PaymentStrategyFactory;
import com.miguel.ecommerce.payment.dto.PaymentCardRequest;
import com.miguel.ecommerce.payment.dto.PaymentCardResponse;
import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.dto.PaymentResponse;
import com.miguel.ecommerce.payment.entity.Payment;
import com.miguel.ecommerce.payment.entity.PaymentCard;
import com.miguel.ecommerce.payment.entity.PaymentStatus;
import com.miguel.ecommerce.payment.mapper.PaymentMapper;
import com.miguel.ecommerce.payment.repository.PaymentCardRepository;
import com.miguel.ecommerce.payment.repository.PaymentRepository;
import com.miguel.ecommerce.payment.service.PaymentService;
import com.miguel.ecommerce.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentCardRepository cardRepository;
    private final OrderRepository orderRepository;
    private final PaymentStrategyFactory strategyFactory;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse processPayment(Long userId, PaymentRequest request) {
        Order order = orderRepository.findByIdAndUserId(request.orderId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Order is not eligible for payment");
        }

        if (paymentRepository.existsByOrderIdAndStatus(request.orderId(), PaymentStatus.APPROVED)) {
            throw new BusinessException("Order already paid");
        }

        PaymentStrategy strategy = strategyFactory.getStrategy(request.method());
        PaymentResult result = strategy.process(request);

        Payment payment = Payment.builder()
                .order(order)
                .method(request.method())
                .amount(order.getTotal())
                .status(result.success() ? PaymentStatus.APPROVED : PaymentStatus.REFUSED)
                .transactionCode(result.transactionCode())
                .build();

        if (result.success()) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        }

        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentCardResponse> findCardsByUserId(Long userId) {
        return paymentMapper.toCardResponseList(cardRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    public PaymentCardResponse addCard(Long userId, PaymentCardRequest request) {
        if (Boolean.TRUE.equals(request.isDefault())) {
            cardRepository.findAllByUserId(userId).forEach(card -> {
                card.setIsDefault(false);
                cardRepository.save(card);
            });
        }

        PaymentCard card = PaymentCard.builder()
                .user(new User() {{ setId(userId); }})
                .cardHolderName(request.cardHolderName())
                .lastFourDigits(request.cardNumber().substring(request.cardNumber().length() - 4))
                .expiryMonth(request.expiryMonth())
                .expiryYear(request.expiryYear())
                .brand(request.brand())
                .isDefault(Boolean.TRUE.equals(request.isDefault()))
                .build();

        return paymentMapper.toCardResponse(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void removeCard(Long userId, Long cardId) {
        PaymentCard card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", cardId));
        cardRepository.delete(card);
    }
}
