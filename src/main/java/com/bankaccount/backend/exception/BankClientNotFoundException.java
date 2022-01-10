package com.bankaccount.backend.exception;

public class BankClientNotFoundException extends RuntimeException {
    

    public BankClientNotFoundException(String msg){
        super(msg);
    }
}
