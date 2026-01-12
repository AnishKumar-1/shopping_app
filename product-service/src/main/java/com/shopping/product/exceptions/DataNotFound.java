package com.shopping.product.exceptions;

public class DataNotFound extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DataNotFound(String message){
        super(message);
    }
}
