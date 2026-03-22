package com.miguel.ecommerce.validator.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context ){
        if (phone == null|| phone.isBlank()) return false;

        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 10 || cleaned.length() > 11) return false;

        String ddd = cleaned.substring(0, 2);
        int dddNumber = Integer.parseInt(ddd);

        if (cleaned.length() == 11 && cleaned.charAt(2) != '9') return false;

        return true;
    }
}
