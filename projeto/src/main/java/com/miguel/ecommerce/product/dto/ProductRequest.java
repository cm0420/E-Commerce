package com.miguel.ecommerce.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        String name,
        String description,
        @NotNull
        @DecimalMin(value = "0.01", message = "price must be greater than zero")
        BigDecimal price,
        @NotBlank
        String sku,
        String imageUrl,
        @NotNull
        Long categoryId,
        @NotNull
        @DecimalMin(value = "0.01", message = "price must be greater than zero")
        BigDecimal costPrice



) {
}
