package com.miguel.ecommerce.payment.controller;

import com.miguel.ecommerce.payment.dto.PaymentCardRequest;
import com.miguel.ecommerce.payment.dto.PaymentCardResponse;
import com.miguel.ecommerce.payment.dto.PaymentRequest;
import com.miguel.ecommerce.payment.dto.PaymentResponse;
import com.miguel.ecommerce.payment.service.PaymentService;
import com.miguel.ecommerce.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> processPayment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(user.getId(), request));
    }


    @GetMapping(value = "/cards", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentCardResponse>> findCards(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.findCardsByUserId(user.getId()));
    }

    @PostMapping(value = "/cards",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentCardResponse> addCard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PaymentCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.addCard(user.getId(), request));
    }

    @DeleteMapping(value = "/cards/{id}")
    public ResponseEntity<Void> removeCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        paymentService.removeCard(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
