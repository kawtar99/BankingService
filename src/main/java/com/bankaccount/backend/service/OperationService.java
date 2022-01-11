package com.bankaccount.backend.service;

import java.util.List;

import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.repository.AccountRepository;
import com.bankaccount.backend.repository.OperationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    

    private OperationRepository operationRepository;
    private AccountRepository accountRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository, AccountRepository accountRepository){
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    public List<Operation> listByAccount(Long id) throws AccountNotFoundException{
        if (!accountRepository.findById(id).isPresent()){
            throw new AccountNotFoundException("Account with id :" + id + "is not found, can't access operations.");
        }
        return operationRepository.findByAccountId(id);
    }

    // To Do : get balance at time t
   
}
