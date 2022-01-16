package com.bankaccount.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.BankClient;

import org.junit.jupiter.api.Test;

public class BankClientRepositoryTest {

    private BankClientRepository bankClientRepository = new BankClientRepository();

    @Test
    void testSave() {
        BankClient bankClient = new BankClient("fn", "ln");
        bankClient.setId(1l);
        assertEquals(bankClientRepository.getBankClients().size(), 0);
        bankClientRepository.save(bankClient);
        assertEquals(bankClientRepository.getBankClients().size(), 1);
        assertEquals(bankClientRepository.getBankClients().get(0).getId(), 1l);
    }

    @Test
    void testFindByIdExistant() {
        BankClient bankClient = new BankClient("fn", "ln");
        bankClient.setId(1l);
        List<BankClient> bankClients = new ArrayList<BankClient>();
        bankClients.add(bankClient);
        bankClientRepository.setBankClients(bankClients);
        Optional<BankClient> optional = bankClientRepository.findById(1l);
        assertTrue(optional.isPresent());
        assertEquals(optional.get().getId(), 1l);
    }

    @Test
    void testFindByIdNotExistant() {
        Optional<BankClient> optional = bankClientRepository.findById(1l);
        assertFalse(optional.isPresent());
    }

}
