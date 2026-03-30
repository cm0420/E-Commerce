package com.miguel.ecommerce.payment.Strategy;

import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.entity.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class PixPaymentStrategy implements PaymentStrategy{
    @Override
    public PaymentResult process(PaymentRequest request) {
        String pixKey = "PIX-" + UUID.randomUUID();
        return new PaymentResult(true, pixKey);
    }
    @Override
    public PaymentMethod getMethod() { return PaymentMethod.PIX; }
}
