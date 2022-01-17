package com.bankaccount.backend.entity;

import java.time.LocalDateTime;


public class Operation {

    static long ID_GENERATOR = 0;
    
    private Long id;
    
    private String operationName;
    private LocalDateTime date;
    private float amount;
    //private float balance;
    private Account account;

    


    public Operation(){
        this.id = ++ID_GENERATOR;
    }

    public Operation(String operationName, LocalDateTime date, float amount, Account account){
        this.id = ++ID_GENERATOR;
        this.operationName = operationName;
        this.date = date;
        this.amount = amount;
        this.account = account;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public LocalDateTime getLocalDateTime() {
        return date;
    }
    public void setLocalDateTime(LocalDateTime date) {
        this.date = date;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
