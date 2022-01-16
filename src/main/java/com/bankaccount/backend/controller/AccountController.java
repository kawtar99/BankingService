package com.bankaccount.backend.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.exception.IllegalOperationException;
import com.bankaccount.backend.service.AccountService;

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
@RequestMapping("/accounts")
public class AccountController {
 
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }
    

    @RequestMapping( value = "/", method = RequestMethod.GET)
	public List<Account> list(){
		return accountService.list();
	}

    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
	public Account read(@PathVariable(value="id") Long id) throws AccountNotFoundException {
		return accountService.read(id);
	}

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody Account account){
        return accountService.create(account);
    }
	
	@RequestMapping( value = "/{id}", method = RequestMethod.PUT )
	public Account update(@PathVariable(value="id") Long id, @RequestBody Account account) throws AccountNotFoundException{
		return accountService.update(id,account);
	}

    @RequestMapping(value = "/{id}/withdraw/{amount}", method = RequestMethod.POST)
    public Operation withdraw(@PathVariable(value = "id") Long id, @PathVariable(value = "amount") float amount) throws IllegalOperationException{
        Account account = accountService.read(id);
        return accountService.withdrawMoney(account, amount);
    }

    @RequestMapping(value = "/{id}/deposit/{amount}", method = RequestMethod.POST)
    public Operation makeDeposit(@PathVariable(value = "id") Long id, @PathVariable(value = "amount") float amount) throws IllegalOperationException{
        Account account = accountService.read(id);
        return accountService.saveMoney(account, amount);
    }

    @RequestMapping(value = "/{id}/balance", method = RequestMethod.GET)
    public float getBalance(@PathVariable(value = "id") Long id) throws AccountNotFoundException{
        return accountService.getBalance(id);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public void handleAccountNotFound(AccountNotFoundException exception, HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(IllegalOperationException.class)
    public void handleIllegalOperationexception(IllegalOperationException exception, HttpServletResponse response) throws IOException{
        response.sendError(400, exception.getMessage());
    }
}
