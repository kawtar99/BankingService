package com.bankaccount.backend.service;

import java.util.Optional;

import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.exception.BankClientNotFoundException;
import com.bankaccount.backend.repository.BankClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankClientService {

    private BankClientRepository bankClientRepository;

    @Autowired
    public BankClientService(BankClientRepository bankClientRepository){
        this.bankClientRepository = bankClientRepository;
    }

    public BankClient create(BankClient bankClient) {
        return bankClientRepository.save(bankClient);
    }

    public BankClient read(Long id) throws BankClientNotFoundException {
        Optional<BankClient> optional = bankClientRepository.findById(id);
        if(!optional.isPresent()){
            throw new BankClientNotFoundException("Client with id : " + id + " is not found");
        }
        return optional.get();
    }
    
}
