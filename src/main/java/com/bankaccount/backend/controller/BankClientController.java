package com.bankaccount.backend.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.bankaccount.backend.entity.BankClient;
import com.bankaccount.backend.exception.BankClientNotFoundException;
import com.bankaccount.backend.service.BankClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public BankClient create(@RequestBody BankClient bankClient){
        return bankClientService.create(bankClient);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BankClient read(@PathVariable(value = "id") Long id) throws BankClientNotFoundException{
        return bankClientService.read(id);
    }


    @ExceptionHandler(BankClientNotFoundException.class)
    public void handleBankClientNotFound(BankClientNotFoundException exception, HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
