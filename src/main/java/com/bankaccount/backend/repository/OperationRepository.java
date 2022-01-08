package com.bankaccount.backend.repository;

import java.util.List;

import com.bankaccount.backend.entity.Operation;

import org.springframework.data.repository.CrudRepository;


public interface OperationRepository extends CrudRepository<Operation, Long> {
    
    List<Operation> findByAccountId(Long id);
}
