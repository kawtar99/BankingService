package com.bankaccount.backend.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.service.OperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operations")
public class OperationController {

    public OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService){
        this.operationService = operationService;
    }
    
    @RequestMapping(value = "/history/{id}", method = RequestMethod.GET)
    public Iterable<Operation> listByAccount(@PathVariable(value = "id") Long id) throws AccountNotFoundException{
        return operationService.listByAccount(id);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public void handleAccountNotFound(AccountNotFoundException exception, HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
