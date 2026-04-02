package com.miguel.ecommerce.order.dto;

import com.miguel.ecommerce.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse (
        Long id,
        Long userId,
        Long addressId,
        OrderStatus status,
        BigDecimal total,
        BigDecimal shippingCost,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
}
