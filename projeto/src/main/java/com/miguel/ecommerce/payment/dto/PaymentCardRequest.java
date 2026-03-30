package com.miguel.ecommerce.payment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentCardRequest(
        @NotBlank String cardHolderName,
        @NotBlank String cardNumber,
        @NotNull @Min(1) @Max(12) Integer expiryMonth,
        @NotNull Integer expiryYear,
        @NotBlank String cvv,
        @NotBlank String brand,
        Boolean isDefault
) {
}
