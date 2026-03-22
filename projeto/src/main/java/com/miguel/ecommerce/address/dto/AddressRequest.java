package com.miguel.ecommerce.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank String street,
        @NotBlank String number,
        String complement,        // sem @NotBlank — opcional
        @NotBlank String district,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank @Size(min = 8, max = 8) String zipCode,
        @NotNull Long userId



) {
}
