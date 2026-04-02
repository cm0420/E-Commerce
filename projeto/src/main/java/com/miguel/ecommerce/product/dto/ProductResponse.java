package com.miguel.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal costPrice,
        String sku,
        String imageUrl,
        Boolean isActive,
        Long categoryId

) {
}
