package com.bankaccount.backend.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Operation {
    
    @Id
	@GeneratedValue
    private Long id;
    
    private String operationName;

    private Date date;
    private float amount;
    private float balance;

    @ManyToOne
    private Account account;


    public Operation(){}

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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
