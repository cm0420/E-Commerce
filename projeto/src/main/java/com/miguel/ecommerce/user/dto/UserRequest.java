package com.miguel.ecommerce.user.dto;

import com.miguel.ecommerce.user.entity.Role;
import com.miguel.ecommerce.validator.phone.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequest(

        @NotBlank
        String firstName,

        @NotBlank
        @ValidPhone
        String phoneNumber,

        @NotBlank
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        @Size(min = 11, max= 11)
        @CPF
        String cpf,

        @NotNull
        Role role




) {
}
