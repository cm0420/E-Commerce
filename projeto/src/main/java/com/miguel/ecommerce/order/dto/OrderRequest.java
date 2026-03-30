package com.miguel.ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull
        Long addressId
) {
}
