package com.bankaccount.backend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bankaccount.backend.entity.BankClient;

import org.springframework.stereotype.Repository;

@Repository
public class BankClientRepository {

    List<BankClient> bankClients;

    public List<BankClient> getBankClients() {
        return bankClients;
    }

    public void setBankClients(List<BankClient> bankClients) {
        this.bankClients = bankClients;
    }

    public BankClientRepository(){
        bankClients = new ArrayList<>();
    }

    public BankClient save(BankClient bankClient) {
        bankClients.add(bankClient);
        return bankClient;
    }

    public Optional<BankClient> findById(Long id) {
        for(int i =0; i< bankClients.size(); i++){
            if (bankClients.get(i).getId() == id){
                return Optional.of(bankClients.get(i));
            }
        }
        return Optional.empty();
    }
    
}
