package com.miguel.ecommerce.payment.service;

import com.miguel.ecommerce.payment.dto.PaymentCardRequest;
import com.miguel.ecommerce.payment.dto.PaymentCardResponse;
import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(Long userId, PaymentRequest request);
    List<PaymentCardResponse> findCardsByUserId(Long userId);
    PaymentCardResponse addCard(Long userId, PaymentCardRequest request);
    void removeCard(Long userId, Long cardId);
}
