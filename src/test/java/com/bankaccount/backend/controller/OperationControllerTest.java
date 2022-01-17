package com.bankaccount.backend.controller;

import java.util.ArrayList;
import java.util.List;

import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.service.OperationService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OperationController.class)
public class OperationControllerTest {
    @MockBean
    private OperationService operationService;

    @Autowired
    private MockMvc mockMvc;

    private List<Operation> operations;

    private Long accountId = 1l;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        operations = new ArrayList<>();
    }

    @AfterEach
	void tearDown(){
		operations = null;
	}


    @Test
    @DisplayName("Should list all account operations When making GET request to endpoint - /operations/history/{id}")
    public void shouldListAccountOperations() throws Exception {
        operations.add(new Operation());
        operations.add(new Operation());
        given(operationService.listByAccount(accountId)).willReturn(operations);

        this.mockMvc.perform(get("/operations/history/{id}", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("Should display 0 operations to a blank account When making GET request to endpoint - /operations/history/{id}")
    public void shouldListBlankAccountOperations() throws Exception {
        given(operationService.listByAccount(accountId)).willReturn(operations);

        this.mockMvc.perform(get("/operations/history/{id}", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("Should display 0 operations to a blank account When making GET request to endpoint - /operations/history/{id}")
    public void shouldNotListOperationsForInvalidAccount() throws Exception {
        given(operationService.listByAccount(accountId)).willThrow(AccountNotFoundException.class);

        this.mockMvc.perform(get("/operations/history/{id}", accountId))
            .andExpect(status().isNotFound());
    }
}
