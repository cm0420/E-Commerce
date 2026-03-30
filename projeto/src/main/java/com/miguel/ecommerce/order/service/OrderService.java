package com.miguel.ecommerce.order.service;

import com.miguel.ecommerce.order.dto.OrderRequest;
import com.miguel.ecommerce.order.dto.OrderResponse;

import java.util.List;

public interface OrderService{
    OrderResponse createOrder(Long userId, OrderRequest orderRequest);
    List<OrderResponse> findAllByUserId(Long userId);
    OrderResponse findById(Long userID, Long orderId);
    OrderResponse cancelOrder(Long userId, Long orderId);

}
