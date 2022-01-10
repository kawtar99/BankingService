package com.bankaccount.backend.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.Account;

import org.springframework.stereotype.Repository;


@Repository
public class AccountRepository  {
    
    private List<Account> accounts;

    public AccountRepository(){
        accounts = new ArrayList<Account>();
    }

    public Account save(Account account) {
        accounts.add(account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        for(int i =0; i< accounts.size(); i++){
            if (accounts.get(i).getId() == id){
                return Optional.of(accounts.get(i));
            }
        }
        return Optional.empty();
    }

    public List<Account> findAll() {
        return accounts;
    }


}
