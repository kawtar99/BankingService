package com.bankaccount.backend.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.exception.AccountAlreadyCreatedException;

import org.springframework.stereotype.Repository;


@Repository
public class AccountRepository  {
    
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public AccountRepository(){
        accounts = new ArrayList<Account>();
    }

    public Account save(Account account) throws AccountAlreadyCreatedException{
        if (accounts.contains(account)){
            throw new AccountAlreadyCreatedException("Account with id :"+ account.getId() + "has already been created.");
        }
        accounts.add(account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        return accounts.stream().filter((acc) -> (acc.getId() == id)).findAny();
    }

    public List<Account> findAll() {
        return accounts;
    }

    public void deleteAll(){
        accounts = null;
        accounts = new ArrayList<>();
    }
}
