package com.bankaccount.backend.entity;


public class Account {
    
    static long ID_GENERATOR = 0;

	private Long id;
    private float balance;
    private BankClient client;

    @SuppressWarnings("unused")
    private Account(){
        this.id = ++ID_GENERATOR;
    }

    public Account(float balance){
        this.id = ++ID_GENERATOR;
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

}
