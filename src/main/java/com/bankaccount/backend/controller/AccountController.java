package com.bankaccount.backend.controller;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public Iterable<Account> list(){
		return accountService.list();
	}

    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
	public Account read(@PathVariable(value="id") Long id){
		return accountService.read(id);
	}

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Account create(@RequestBody Account account){
        return accountService.create(account);
    }
	
	@RequestMapping( value = "/{id}", method = RequestMethod.PUT )
	public Account update(@PathVariable(value="id") Long id, @RequestBody Account account){
		return accountService.update(id,account);
	}

    @RequestMapping(value = "/{id}/withdraw/{amount}", method = RequestMethod.POST)
    public Account withdraw(@PathVariable(value = "id") Long id, @PathVariable(value = "amount") float amount){
        Account account = accountService.read(id);
        return accountService.withdrawMoney(account, amount);
    }

    @RequestMapping(value = "/{id}/deposit/{amount}", method = RequestMethod.POST)
    public Account makeDeposit(@PathVariable Long id, @PathVariable(value = "amount") float amount){
        Account account = accountService.read(id);
        return accountService.saveMoney(account, amount);
    }

}
