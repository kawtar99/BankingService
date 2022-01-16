package com.bankaccount.backend.controller;

import com.bankaccount.backend.service.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OperationController.class)
public class OperationControllerTest {
    @Mock
    private OperationService operationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should list all account operations When making GET request to endpoint - /operations/history/{id}")
    public void shouldListAccountOperations() throws Exception {
        
    }
}
