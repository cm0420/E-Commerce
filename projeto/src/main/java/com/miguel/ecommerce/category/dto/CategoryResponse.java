package com.miguel.ecommerce.category.dto;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt


) {
}
