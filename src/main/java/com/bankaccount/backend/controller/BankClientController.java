package com.bankaccount.backend.controller;

import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.service.BankClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class BankClientController {
    
    private BankClientService bankClientService;

    @Autowired
    public BankClientController(BankClientService bankClientService){
        this.bankClientService = bankClientService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BankClient create(@RequestBody BankClient bankClient){
        return bankClientService.create(bankClient);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BankClient read(@PathVariable(value = "id") Long id){
        return bankClientService.read(id);
    }
}
