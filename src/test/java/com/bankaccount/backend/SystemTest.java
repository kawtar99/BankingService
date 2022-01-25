package com.bankaccount.backend;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SystemTest {

    @Autowired
	private AccountRepository accountRepository;
/*
    @Autowired
    private BankClientRepository bankClientRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AccountService accountService;
*/
	@Autowired
	private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void getAllAccounts() throws Exception{
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        Account account2 = new Account();
        BankClient client1 = new BankClient("first", "client");
        BankClient client2 = new BankClient("second", "client");
        account1.setClient(client1);
        account2.setClient(client2);
        accounts.add(account1);
        accounts.add(account2);
        
        //accountRepository.save(account1);
        accountRepository.setAccounts(accounts);

        mockMvc.perform(get("/accounts/"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", is(accounts.size())));

    }

    @Test
    public void createAnAccount() throws Exception {
        Account testAccount = new Account();
        testAccount.setId(33l);
        testAccount.setClient(new BankClient("test", "client"));

        mockMvc.perform(post("/accounts/")
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(testAccount)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(33)));
    }

    @Test
    public void getAnAccountById() throws Exception{
        Account account = new Account();
        account.setId(44l);
        accountRepository.setAccounts(List.of(account));

        mockMvc.perform(get("/accounts/{accountId}", 44))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(44)));
    }


    @Test
    public void getAnAccountByIdNotFound() throws Exception{
        mockMvc.perform(get("/accounts/{accountId}", 105))
               .andExpect(status().isNotFound());
    }

}
