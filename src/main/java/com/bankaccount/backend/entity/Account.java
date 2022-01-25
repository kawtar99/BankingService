package com.bankaccount.backend.entity;


public class Account {
    
    static long ID_GENERATOR = 0;

	private Long id;
    private BankClient client;

    public Account(){
        this.id = ++ID_GENERATOR;
        this.client = new BankClient();
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



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

   
}
