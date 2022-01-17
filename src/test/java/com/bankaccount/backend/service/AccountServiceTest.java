package com.bankaccount.backend.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.exception.IllegalOperationException;
import com.bankaccount.backend.repository.AccountRepository;
import com.bankaccount.backend.repository.BankClientRepository;
import com.bankaccount.backend.repository.OperationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BankClientRepository bankClientRepository;

    @Mock
    private OperationRepository operationRepository;

    private Account testAccount;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(accountRepository, bankClientRepository, operationRepository);
        testAccount = new Account();
        testAccount.setId(1l);
        Mockito.when(accountRepository.findById(1l)).thenReturn(Optional.of(testAccount));
    }


    @Test
    void testGetBalance() {
        LocalDateTime date1 = LocalDateTime.of(2021, Month.DECEMBER, 23, 0, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2021, Month.DECEMBER, 31, 0, 0, 0);
        LocalDateTime date3 = LocalDateTime.of(2022, Month.JANUARY, 4, 0, 0, 0);
      
        List<Operation> testOperations = new ArrayList<>();
        testOperations.add(new Operation("DEPOSIT",date1, 3000l, testAccount));
        testOperations.add(new Operation("DEPOSIT",date2, 1500l, testAccount));
        testOperations.add(new Operation("WITHDRAWAL",date3, -500l, testAccount));

        Mockito.when(operationRepository.findByAccountId(1l)).thenReturn(testOperations);

        assertEquals(accountService.getBalance(1l), 4000l);
    }

    @Test
    void testGetBalanceFutureOperations() {

        LocalDateTime date1 = LocalDateTime.of(2022, Month.JANUARY, 4, 0, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2022, Month.JANUARY, 14, 0, 0, 0);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        
        List<Operation> testOperations = new ArrayList<>();
        testOperations.add(new Operation("DEPOSIT",date1, 3000l, testAccount));
        testOperations.add(new Operation("DEPOSIT",date2, 1500l, testAccount));
        testOperations.add(new Operation("WITHDRAWAL",tomorrow, -500l, testAccount));

        Mockito.when(operationRepository.findByAccountId(1l)).thenReturn(testOperations);

        assertEquals(accountService.getBalance(1l), 4500l);
    }

    @Test
    void getBalanceBlankAccount(){
        Mockito.when(operationRepository.findByAccountId(1l)).thenReturn(new ArrayList<>());
        assertEquals(accountService.getBalance(1l), 0l);
    }

    @Test
    void testReadValidId() {
        Account result = accountService.read(1L);
        assertEquals(result.getId(), 1l);
    }

    @Test
    void testReadNotValidId(){
        Throwable exception = assertThrows(
            AccountNotFoundException.class, () -> {
                accountService.read(2l);
            }
        );
        assertEquals("Account with id :2 is not found.", exception.getMessage());
    }

    @Test
    void testSaveMoney() {
        Operation operation = new Operation("DEPOSIT", LocalDateTime.now(), 500, testAccount);
        Mockito.when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        Operation result = accountService.saveMoney(testAccount, 500);
        assertEquals(result.getAmount(), 500);
    }

    @Test
    void testSaveMoneyInvalidAmount() {
        Throwable exception = assertThrows(
            IllegalOperationException.class, () -> {
                accountService.saveMoney(testAccount, -100);
            }
        );
        assertEquals("The amount should be positive.", exception.getMessage());     
    }

    @Test
    void testWithdrawMoney() {
        // Setting the balance to 3000
        List<Operation> testOperations = new ArrayList<>();
        LocalDateTime date = LocalDateTime.of(2022, Month.JANUARY, 4, 0, 0, 0);
        testOperations.add(new Operation("DEPOSIT", date, 3000l, testAccount));
        Mockito.when(operationRepository.findByAccountId(testAccount.getId())).thenReturn(testOperations);

        
        Operation operation = new Operation("WITHDRAWAL", LocalDateTime.now(), -100, testAccount);
        Mockito.when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        Operation result = accountService.withdrawMoney(testAccount, 100);
        assertEquals(result.getAmount(), -100);
    }

    void testWithdrawMoneyInvalidOperation() {
        // Setting the balance to 3000
        List<Operation> testOperations = new ArrayList<>();
        LocalDateTime date = LocalDateTime.of(2022, Month.JANUARY, 4, 0, 0, 0);
        testOperations.add(new Operation("DEPOSIT", date, 100l, testAccount));
        Mockito.when(operationRepository.findByAccountId(testAccount.getId())).thenReturn(testOperations);

        Throwable exception = assertThrows(
            IllegalOperationException.class, () -> {
                accountService.withdrawMoney(testAccount, 200);
            }
        );
        assertEquals("Your account balance is lower than the amount desired.", exception.getMessage());
    }
}
