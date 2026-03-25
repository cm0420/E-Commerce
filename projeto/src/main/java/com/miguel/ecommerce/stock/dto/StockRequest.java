package com.miguel.ecommerce.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StockRequest(
        @NotNull @Min(1) Integer quantity
        ) {
}
