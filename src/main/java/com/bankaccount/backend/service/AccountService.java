package com.bankaccount.backend.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountNotFoundException;
import com.bankaccount.backend.exception.IllegalOperationException;
import com.bankaccount.backend.repository.AccountRepository;
import com.bankaccount.backend.repository.BankClientRepository;
import com.bankaccount.backend.repository.OperationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private AccountRepository accountRepository;
    private BankClientRepository bankClientRepository;
    private OperationRepository operationRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository, BankClientRepository bankClientRepository, OperationRepository operationRepository){
        this.accountRepository = accountRepository;
        this.bankClientRepository = bankClientRepository;
        this.operationRepository = operationRepository;
    }

    public Account read(Long id) throws AccountNotFoundException {
		Optional<Account> optional = accountRepository.findById(id);
        if(!optional.isPresent()){
            throw new AccountNotFoundException("Account with id :" + id + " is not found.");
        }
        return optional.get();
	}

	public Account create(Account account) {
        bankClientRepository.save(account.getClient());
		return accountRepository.save(account);
	}
    
    public Account saveMoney(Account account, float amount) throws IllegalOperationException{
        if (amount < 0){
            throw new IllegalOperationException("The amount should be positive");
        }
        account.setBalance(account.getBalance() + amount);
        Operation operation = new Operation();
        operation.setOperationName("DEPOSIT");
        operation.setAmount(amount);
        operation.setBalance(account.getBalance());
        operation.setDate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        return accountRepository.save(account);
    }

    public Account withdrawMoney(Account account, float amount) throws IllegalOperationException{
        if (amount < 0){
            throw new IllegalOperationException("The amount should be positive");
        }

        if (amount > account.getBalance()){
            throw new IllegalOperationException("Your account balance is lower than the amount desired");
        }
        account.setBalance(account.getBalance() - amount);
        Operation operation = new Operation();
        operation.setOperationName("WITHDRAWAL");
        operation.setAmount(-amount);
        operation.setBalance(account.getBalance());
        operation.setDate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        return accountRepository.save(account);
    }

    public Account update(Long id, Account update) throws AccountNotFoundException {
        Account account = read(id);
		account.setBalance(update.getBalance());
		return accountRepository.save(account);
    }

    public List<Account> list() {
        return accountRepository.findAll();
    }


    // TO DO : Exception handling, Repository, tests , service, get balance from operations 

}
