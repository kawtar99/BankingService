package com.bankaccount.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.repository.AccountRepository;
import com.bankaccount.backend.repository.OperationRepository;
import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OperationServiceTest {

    @InjectMocks
    private OperationService operationService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        operationService = new OperationService(operationRepository, accountRepository);
    }

    @Test
    void testListByAccountStandard() {
        
        Mockito.when((accountRepository.findById(1l))).thenReturn(Optional.of(new Account()));

        List<Operation> operations = new ArrayList<>();
        Mockito.when(operationRepository.findByAccountId(1l)).thenReturn(operations);

        List<Operation> result = operationService.listByAccount(1l);

        assertEquals(result.size(), 0);
    }

    @Test
    void testListByAccountForNotFoundAccount(){
        Mockito.when(accountRepository.findById(1l)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            AccountNotFoundException.class, () -> {
               operationService.listByAccount(1l);
            }
        );
        assertEquals("Account with id : 1 is not found, can't access operations.", exception.getMessage());
    }
}
