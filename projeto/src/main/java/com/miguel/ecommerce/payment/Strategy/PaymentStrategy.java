package com.miguel.ecommerce.payment.Strategy;

import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.entity.PaymentMethod;

public interface PaymentStrategy {
    PaymentResult process(PaymentRequest request);
    PaymentMethod getMethod();
}
