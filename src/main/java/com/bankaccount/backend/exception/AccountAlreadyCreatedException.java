package com.bankaccount.backend.exception;

public class AccountAlreadyCreatedException extends RuntimeException {
     
    public AccountAlreadyCreatedException(String msg){
        super(msg);
    }
}
