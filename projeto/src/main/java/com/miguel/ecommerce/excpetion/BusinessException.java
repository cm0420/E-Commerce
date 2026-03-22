package com.miguel.ecommerce.excpetion;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }

}
