package com.miguel.ecommerce.stock.dto;


import java.time.LocalDateTime;

public record StockResponse(
        Long id,
        Integer quantity,
        Long productId,
        LocalDateTime createdAt
) {
}
