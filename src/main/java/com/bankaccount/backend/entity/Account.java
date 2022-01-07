package com.bankaccount.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity 
public class Account {
    
    @Id 
    @GeneratedValue
	private Long id;

    private float balance;

    @OneToOne
    private BankClient client;

    @SuppressWarnings("unused")
    private Account(){}

    public Account(float balance){
        this.balance = balance;
    }

    
    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankClient getClient() {
        return client;
    }

    public void setClient(BankClient client) {
        this.client = client;
    }

    public void addToBalance(float amount){
        if (amount < 0){
            throw new RuntimeException("The amount should be positive");
        }
        balance = balance + amount;
    }
    
    public void retrieveFromBalance(float amount){
        if (amount < 0){
            throw new RuntimeException("The amount should be positive");
        }

        if (amount > balance){
            throw new RuntimeException("Your account balance is lower than the amount desired");
        }

        else{
            balance = balance - amount;
        }
    }
}
