package com.bankaccount.backend.entity;


public class Account {
    
    static long ID_GENERATOR = 0;

	private Long id;
    private BankClient client;

    public Account(){
        this.id = ++ID_GENERATOR;
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
