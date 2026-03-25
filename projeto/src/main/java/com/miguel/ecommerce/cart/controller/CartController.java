package com.miguel.ecommerce.cart.controller;

import com.miguel.ecommerce.cart.dto.CartItemRequest;
import com.miguel.ecommerce.cart.dto.CartResponse;
import com.miguel.ecommerce.cart.service.CartService;
import com.miguel.ecommerce.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }

    @PostMapping(value= "/items",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse>addItem(@AuthenticationPrincipal User user,@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(user.getId(), request));
    }

    @DeleteMapping(value = "/items/{productId}")
    public ResponseEntity<Void> removeItem(@AuthenticationPrincipal User user, @PathVariable("productId") Long productId) {
        cartService.removeItem(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/items/{productId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> updateItemQuantity(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(
                cartService.updateItemQuantity(user.getId(), productId, request));
    }

}
