package com.bankaccount.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.Account;

import org.junit.jupiter.api.Test;

public class AccountRepositoryTest {
    
    private AccountRepository accountRepository = new AccountRepository();


    @Test
    public void initialisedIntoEmptyRepository(){
        assertEquals(accountRepository.getAccounts().size(), 0);
    }

    @Test
    public void saveIntoRepository(){
        Account account = new Account();
        accountRepository.save(account);
        assertEquals(accountRepository.findAll().size(), 1);
        assertEquals(accountRepository.getAccounts().get(0).getId(), account.getId());
    }

    @Test
    public void findByIdExistant(){
        Account account = new Account();
        account.setId(1l);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accountRepository.setAccounts(accounts);
        Optional<Account> optional = accountRepository.findById(1l);
        assertTrue(optional.isPresent());
        assertEquals(optional.get().getId(), 1l);
    }

    @Test
    public void findByIdNotExistant(){
        Account account = new Account();
        account.setId(1l);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accountRepository.setAccounts(accounts);
        Optional<Account> optional = accountRepository.findById(2l);
        assertFalse(optional.isPresent());
    }
}
