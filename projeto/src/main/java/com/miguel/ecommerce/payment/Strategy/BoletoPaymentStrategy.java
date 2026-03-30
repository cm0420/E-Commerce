package com.miguel.ecommerce.payment.Strategy;

import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.entity.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class BoletoPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult process(PaymentRequest request) {
        String boletoCode = "Boleto-"+ UUID.randomUUID();
        return new PaymentResult(true, boletoCode);
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.BOLETO;
    }
}
