package com.shopping.product.exceptions;

public class DuplicateResourceException extends RuntimeException{

    public DuplicateResourceException(String message){
        super(message);
    }
}
