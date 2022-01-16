package com.bankaccount.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;

import org.junit.jupiter.api.Test;

public class OperationRepositoryTest {

    private OperationRepository operationRepository= new OperationRepository();

    @Test
    void testFindByAccountIdOnEmptyAccount() {
        List<Operation> operations = operationRepository.findByAccountId(2l);
        assertEquals(operations.size(), 0);
    }

    @Test
    void testFindByAccountId() {
        Account account = new Account();
        Operation deposit = new Operation();
        deposit.setAmount(1500);
        deposit.setDate(new Date());
        deposit.setId(1l);
        deposit.setAccount(account);

        Map<Long, List<Operation>> operations = new HashMap<>();
        List<Operation> list = new ArrayList<>();
        list.add(deposit);
        operations.put(1l, list);
        operationRepository.setOperations(operations);

        List<Operation> myAccountOperations = operationRepository.findByAccountId(1l);
        
        assertEquals(myAccountOperations.size(), 1);
        assertEquals(myAccountOperations.get(0).getId(), 1l);
    }

    @Test
    void testSaveOperation() {
        Account account = new Account();
        Operation deposit = new Operation();
        deposit.setAmount(600);
        deposit.setDate(new Date());
        deposit.setId(1l);
        deposit.setAccount(account);

        operationRepository.save(deposit);
        assertTrue(operationRepository.getOperations().containsKey(account.getId()));
        assertEquals(operationRepository.getOperations().get(account.getId()).size(), 1);
        assertEquals(operationRepository.getOperations().get(account.getId()).get(0).getId(), deposit.getId());
    }
}
