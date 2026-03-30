package com.miguel.ecommerce.payment.dto;

import com.miguel.ecommerce.payment.entity.PaymentMethod;
import com.miguel.ecommerce.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        PaymentMethod method,
        PaymentStatus status,
        BigDecimal amount,
        String transactionCode,
        LocalDateTime createdAt


) {
}
