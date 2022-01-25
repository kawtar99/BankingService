package com.bankaccount.backend;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.repository.AccountRepository;
import com.bankaccount.backend.repository.OperationRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class RequirementsTest {
    
    // This class should test the three requirements of the project:
    // Money deposit
    // Money withdrawal
    // List of previous operations

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private MockMvc mockMvc;

    private static Account testAccount;
    
    @BeforeAll
    public static void setUp(){
        BankClient bankClient = new BankClient("bank", "client");
        bankClient.setId(22l);
        testAccount = new Account();
        testAccount.setId(1l);
        testAccount.setClient(bankClient);
    }

    @Test
    public void makeDepositTest() throws Exception{
        mockMvc.perform(post("/accounts/{id}/deposit/{amount}", 1, 500))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.operationName", is("DEPOSIT")))
               .andExpect(jsonPath("$.amount", is(500.0)))
               .andExpect(jsonPath("$.account.id", is(1)))
               .andExpect(jsonPath("$.account.client.id", is(22)));
    }

    @Test
    public void makeWithdrawalTest() throws Exception{
        mockMvc.perform(post("/accounts/{id}/withdraw/{amount}", 1, 200))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.operationName", is("WITHDRAWAL")))
               .andExpect(jsonPath("$.amount", is(-200.0)))
               .andExpect(jsonPath("$.account.id", is(1)))
               .andExpect(jsonPath("$.account.client.id", is(22)));
    }

    @Test
    public void makeInvalidWithdrawalTest() throws Exception{
        accountRepository.save(testAccount);
        operationRepository.save(new Operation("DEPOSIT", LocalDateTime.now(), 500l, testAccount));
        mockMvc.perform(post("/accounts/{id}/withdraw/{amount}", 1, 1000))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void seeOperationsHistory() throws Exception{
        mockMvc.perform(get("/operations/history/{id}",1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void checkBalanceAfterOperations() throws Exception {
        mockMvc.perform(get("/accounts/{id}/balance", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(800.0)));
        
    }
}
