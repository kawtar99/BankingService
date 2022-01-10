package com.bankaccount.backend.entity;


public class BankClient {
    
    static long ID_GENERATOR = 0;

	private Long id;
	private String firstName;
	private String lastName;

    @SuppressWarnings("unused")
    private BankClient(){
        this.id = ++ID_GENERATOR;
    }

    public BankClient(String firstName, String lastName){
        this.id = ++ID_GENERATOR;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    

}
