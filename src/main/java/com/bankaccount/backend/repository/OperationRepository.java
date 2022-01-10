package com.bankaccount.backend.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankaccount.backend.entity.Operation;

import org.springframework.stereotype.Repository;

@Repository
public class OperationRepository{

    Map<Long, List<Operation>> operations;

    public OperationRepository(){
        operations = new HashMap<>();
    }
    
    public Operation save(Operation operation) {
        Long accountId = operation.getAccount().getId();
        if (operations.containsKey(accountId)){
            operations.get(accountId).add(operation);
        }
        else{
            operations.put(accountId , new ArrayList<Operation>(Arrays.asList(operation)));
        }
        return operation;
    }

    public List<Operation> findByAccountId(Long id){
        return operations.get(id);
    }

}
