package com.bankaccount.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.exception.BankClientNotFoundException;
import com.bankaccount.backend.repository.BankClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BankClientServiceTest {

    @InjectMocks
    private BankClientService bankClientService;

    @Mock
    private BankClientRepository bankClientRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bankClientService = new BankClientService(bankClientRepository);

        BankClient bankClient = new BankClient("fn", "ln");
        bankClient.setId(1l);

        Mockito.when(bankClientRepository.findById(1l))
                .thenReturn(Optional.of(bankClient));
    }

    @Test
    void testReadPresent() {
        
        String firstName = "fn";
        String lastName = "ln";

        BankClient testClient = bankClientService.read(1l);
        
        assertEquals(testClient.getId(), 1l);
        assertEquals(testClient.getFirstName(), firstName);
        assertEquals(testClient.getLastName(), lastName);
    }

    @Test
    void testReadNotPresent(){
        Throwable exception = assertThrows(
            BankClientNotFoundException.class, () -> {
                bankClientService.read(2l);
            }
        );
        assertEquals("Client with id : 2 is not found.", exception.getMessage());
    }
}
