package com.miguel.ecommerce.excpetion;

public record ErrorResponse(
        String error,
        String message
) {
}
