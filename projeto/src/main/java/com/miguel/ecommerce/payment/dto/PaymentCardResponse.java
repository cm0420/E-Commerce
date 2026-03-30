package com.miguel.ecommerce.payment.dto;

public record PaymentCardResponse(
        Long id,
        String cardHolderName,
        String lastFourDigits,
        String brand,
        Integer expiryMonth,
        Integer expiryYear,
        Boolean isDefault
) {
}
