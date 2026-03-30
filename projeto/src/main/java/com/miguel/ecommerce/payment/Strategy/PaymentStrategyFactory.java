package com.miguel.ecommerce.payment.Strategy;

import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.payment.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {
    private final List<PaymentStrategy> strategies;
    public PaymentStrategy getStrategy(PaymentMethod method) {
        return strategies.stream()
                .filter(s -> s.getMethod() == method)
                .findFirst()
                .orElseThrow(() -> new BusinessException("Payment method not supported: " + method));
    }
}
