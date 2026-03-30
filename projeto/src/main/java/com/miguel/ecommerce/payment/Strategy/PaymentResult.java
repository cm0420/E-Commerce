package com.miguel.ecommerce.payment.Strategy;

public record PaymentResult(
        boolean success,
        String transactionCode
) {
}
