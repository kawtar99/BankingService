package com.bankaccount.backend.controller;

import static org.mockito.ArgumentMatchers.any;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.exception.IllegalOperationException;
import com.bankaccount.backend.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = AccountController.class)
@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    @DisplayName("Should create an account When making POST request to endpoint - /accounts/")
    public void shouldCreateBankClient() throws Exception {
        given(accountService.create(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));

        Account account = new Account();

        ResultActions response = this.mockMvc.perform(post("/accounts/")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(account)));
       
		response.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)));
            //.andExpect(jsonPath("$.client.id", is(account.getClient().getId())))
            //.andExpect(jsonPath("$.client.firsName", is(account.getClient().getFirstName())))
            //.andExpect(jsonPath("$.client.lastName", is(account.getClient().getLastName())));
    }

    @Test
    @DisplayName("Should read all the bank accounts When making GET request to endpoint - /accounts/")
    public void shouldReadAllAccounts() throws Exception {
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        given(accountService.list()).willReturn(accounts);

        ResultActions response = this.mockMvc.perform(get("/accounts/"));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(accounts.size())));
    }

    @Test
    @DisplayName("Should read an account depending on their id When making GET request to endpoint - /accounts/{id}")
    public void shouldReadAccountById() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);

        given(accountService.read(accountId)).willReturn(account);

        ResultActions response = this.mockMvc.perform(get("/accounts/{id}", accountId));
       
		response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
           // .andExpect(jsonPath("$.client.id", is(account.getClient().getId())))
            //.andExpect(jsonPath("$.client.firstName", is(account.getClient().getFirstName())))
            //.andExpect(jsonPath("$.client.lastName", is(account.getClient().getLastName())));
    }

    @Test
    @DisplayName("Should read an account depending on their id When making GET request to endpoint - /accounts/{id}")
    public void shouldRead404AccountByIdNotFound() throws Exception {
        Long accountId = 1l;
        given(accountService.read(accountId)).willThrow(AccountNotFoundException.class);
        ResultActions response = this.mockMvc.perform(get("/accounts/{id}", accountId));
       
		response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should withdraw an amount of money from an account depending on their id and the amount When making GET request to endpoint - /accounts/{id}/withdrawal/{amount}")
    public void shouldWithdrawMoney() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = 500.0f;

        Operation operation = new Operation("WITHDRAWAL", new Date(), amount, account);
        operation.setId(2l);

        given(accountService.read(accountId)).willReturn(account);
        given(accountService.getBalance(accountId)).willReturn(3000.0f);
        given(accountService.withdrawMoney(account, amount)).willReturn(operation);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/withdraw/{amount}", accountId, amount));
       
		response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
           // .andExpect(jsonPath("$.date", is(operation.getDate())))
            .andExpect(jsonPath("$.amount", is(500.0)))
            .andExpect(jsonPath("$.operationName", is(operation.getOperationName())))
            .andExpect(jsonPath("$.account.id", is(1)));
    }

    @Test
    @DisplayName("Should not withdraw money from an invalid account When making GET request to endpoint - /accounts/{id}/withdrawal/{amount}")
    public void shouldWithdrawMoneyFromInvalidAccount() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = 500.0f;

        given(accountService.read(accountId)).willThrow(AccountNotFoundException.class);
        given(accountService.withdrawMoney(account, amount)).willThrow(AccountNotFoundException.class);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/withdraw/{amount}", accountId, amount));
       
		response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should withdraw an invalid amount of money from an account depending on their id and the amount When making GET request to endpoint - /accounts/{id}/withdrawal/{amount}")
    public void shouldWithdrawInvalidAmountFromAccount() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = 500.0f;

        given(accountService.read(accountId)).willReturn(account);
        given(accountService.withdrawMoney(account, amount)).willThrow(IllegalOperationException.class);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/withdraw/{amount}", accountId, amount));
       
		response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should save an amount of money into an account depending on their id and the amount When making POST request to endpoint - /accounts/{id}/save/{amount}")
    public void shouldSaveMoney() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = 500.0f;

        Operation operation = new Operation("DEPOSIT", new Date(), amount, account);

        given(accountService.read(accountId)).willReturn(account);
        given(accountService.saveMoney(account, amount)).willReturn(operation);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/deposit/{amount}", accountId, amount));
       
		response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
          //  .andExpect(jsonPath("$.date", is(operation.getDate())))
            .andExpect(jsonPath("$.amount", is(500.0)))
            .andExpect(jsonPath("$.operationName", is(operation.getOperationName())))
            .andExpect(jsonPath("$.account.id", is(1)));
    }

    @Test
    @DisplayName("Should not save money from an invalid account When making GET request to endpoint - /accounts/{id}/deposit/{amount}")
    public void shouldSaveMoneyIntoInvalidAccount() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = 500.0f;

        given(accountService.read(accountId)).willThrow(AccountNotFoundException.class);
        given(accountService.saveMoney(account, amount)).willThrow(AccountNotFoundException.class);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/deposit/{amount}", accountId, amount));
       
		response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not save a negative amount of money from an account depending on their id and the amount When making GET request to endpoint - /accounts/{id}/deposit/{amount}")
    public void shouldSaveNegativeAmountIntoAccount() throws Exception {
        Long accountId = 1l;
        Account account = new Account();
        account.setId(accountId);
        float amount = -500.0f;

        given(accountService.read(accountId)).willReturn(account);
        given(accountService.saveMoney(account, amount)).willThrow(IllegalOperationException.class);

        ResultActions response = this.mockMvc.perform(post("/accounts/{id}/deposit/{amount}", accountId, amount));
       
		response.andExpect(status().isBadRequest());
    }

}
