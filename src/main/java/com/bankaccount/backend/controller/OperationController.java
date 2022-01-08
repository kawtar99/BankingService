package com.bankaccount.backend.controller;

import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.service.OperationService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Iterable<Operation> listByAccount(@PathVariable(value = "id") Long id){
        return operationService.listByAccount(id);
    }
}
