package com.miguel.ecommerce.excpetion;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource, Long Id) {
        super(resource + " " + Id + " not found");
    }
}
