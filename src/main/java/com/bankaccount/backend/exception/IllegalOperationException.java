package com.bankaccount.backend.exception;

public class IllegalOperationException extends RuntimeException {
    
    public IllegalOperationException(String msg){
        super(msg);
    }
}
