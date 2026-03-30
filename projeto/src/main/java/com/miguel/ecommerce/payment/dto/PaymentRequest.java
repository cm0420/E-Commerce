package com.miguel.ecommerce.payment.dto;

import com.miguel.ecommerce.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull Long orderId,
        @NotNull PaymentMethod method,
        Long cardId) {
}
