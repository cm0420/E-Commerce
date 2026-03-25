package com.miguel.ecommerce.cart.service;

import com.miguel.ecommerce.cart.dto.CartItemRequest;
import com.miguel.ecommerce.cart.dto.CartResponse;
import com.miguel.ecommerce.cart.entity.Cart;

public interface CartService {
    Cart getOrCreatCart(Long userId);
    CartResponse buildCartResponse(Cart cart);
    CartResponse getCart(Long userId);
    CartResponse addItem(Long userId, CartItemRequest cartItemRequest);
    CartResponse removeItem(Long userId, Long productId);
    CartResponse updateItemQuantity(Long userId, Long productId, CartItemRequest cartItemRequest);
    void clearCart(Long userId);


}
