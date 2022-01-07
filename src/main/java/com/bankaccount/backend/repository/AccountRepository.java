package com.bankaccount.backend.repository;

import com.bankaccount.backend.entity.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    
}
