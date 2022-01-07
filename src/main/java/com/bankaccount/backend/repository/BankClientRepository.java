package com.bankaccount.backend.repository;

import com.bankaccount.backend.entity.BankClient;

import org.springframework.data.repository.CrudRepository;

public interface BankClientRepository extends CrudRepository<BankClient, Long> {
    
}
