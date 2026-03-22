package com.miguel.ecommerce.address.dto;

import com.miguel.ecommerce.user.entity.User;

import java.time.LocalDateTime;

public record AddressResponse(
        Long id,
        String street,
        String number,
        String complement,
        String district,
        String city,
        String state,
        String zipCode,
        Long userId,
        LocalDateTime createdAt


) {

}
