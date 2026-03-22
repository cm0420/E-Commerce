package com.miguel.ecommerce.user.dto;

import com.miguel.ecommerce.user.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String cpf,
        Role role,
        Boolean isActive,
        LocalDateTime createdAt
) {
}
