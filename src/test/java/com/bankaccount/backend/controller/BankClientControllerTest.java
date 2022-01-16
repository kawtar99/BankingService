package com.bankaccount.backend.controller;

import static org.mockito.ArgumentMatchers.any;

import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.service.BankClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = BankClientController.class)
public class BankClientControllerTest {
    
    @InjectMocks
    private BankClientController bankClientController;

    @MockBean
    private BankClientService bankClientService;
 
   
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        bankClientController = new BankClientController(bankClientService);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(bankClientController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create a bank client When making POST request to endpoint - /clients/")
    void shouldCreateBankClient() throws Exception {
        Mockito.when(bankClientService.create(any(BankClient.class))).thenAnswer((invocation) -> invocation.getArgument(0));
    
        BankClient bankClient = new BankClient("first", "client");
        
        this.mockMvc.perform(post("/clients/")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(bankClient)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName", is(bankClient.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(bankClient.getLastName())));
            
    }

    @Test
    @DisplayName("Should read a bank client depending on their id When making GET request to endpoint - /clients/{id}")
    public void shouldReadBankClientById() throws Exception {
        Long bankClientId = 1l;
        BankClient bankClient = new BankClient("first", "client");
        bankClient.setId(bankClientId);
        given(bankClientService.read(bankClientId)).willReturn(bankClient);

        this.mockMvc.perform(get("/clients/{id}", bankClientId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is(bankClient.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(bankClient.getLastName())));
    }
}
