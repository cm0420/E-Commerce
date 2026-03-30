package com.miguel.ecommerce.payment.Strategy;

import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.entity.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult process(PaymentRequest request) {
        String authCode = "Card-"+ UUID.randomUUID();
        return new PaymentResult(true, authCode);
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.CREDIT_CARD;
    }
}
