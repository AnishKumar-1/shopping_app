package com.shopping.inventory.exceptions;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
