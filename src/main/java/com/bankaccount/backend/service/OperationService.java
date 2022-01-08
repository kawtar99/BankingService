package com.bankaccount.backend.service;

import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.repository.OperationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    

    private OperationRepository operationRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository){
        this.operationRepository = operationRepository;
    }

    public Iterable<Operation> listByAccount(Long id){
        return operationRepository.findByAccountId(id);
    }
}
