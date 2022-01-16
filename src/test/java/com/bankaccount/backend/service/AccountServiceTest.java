package com.bankaccount.backend.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        Calendar cal = Calendar.getInstance();
        cal.set(2021, Calendar.DECEMBER, 23);
        Date date1 = cal.getTime();
        cal.set(2021, Calendar.DECEMBER, 31);
        Date date2 = cal.getTime();
        cal.set(2022, Calendar.JANUARY, 4);
        Date date3 = cal.getTime();
        
        List<Operation> testOperations = new ArrayList<>();
        testOperations.add(new Operation("DEPOSIT",date1, 3000l, testAccount));
        testOperations.add(new Operation("DEPOSIT",date2, 1500l, testAccount));
        testOperations.add(new Operation("WITHDRAWAL",date3, -500l, testAccount));

        Mockito.when(operationRepository.findByAccountId(1l)).thenReturn(testOperations);

        assertEquals(accountService.getBalance(1l), 4000l);
    }

    @Test
    void testGetBalanceFutureOperations() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        cal.set(2021, Calendar.DECEMBER, 23);
        Date date1 = cal.getTime();
        cal.set(2021, Calendar.DECEMBER, 31);
        Date date2 = cal.getTime();

        
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
        Operation operation = new Operation("DEPOSIT", new Date(), 500, testAccount);
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
        Calendar cal = Calendar.getInstance();
        cal.set(2021, Calendar.DECEMBER, 23);
        Date date = cal.getTime();
        testOperations.add(new Operation("DEPOSIT", date, 3000l, testAccount));
        Mockito.when(operationRepository.findByAccountId(testAccount.getId())).thenReturn(testOperations);

        
        Operation operation = new Operation("WITHDRAWAL", new Date(), -100, testAccount);
        Mockito.when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        Operation result = accountService.withdrawMoney(testAccount, 100);
        assertEquals(result.getAmount(), -100);
    }

    void testWithdrawMoneyInvalidOperation() {
        // Setting the balance to 3000
        List<Operation> testOperations = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(2021, Calendar.DECEMBER, 23);
        Date date = cal.getTime();
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
