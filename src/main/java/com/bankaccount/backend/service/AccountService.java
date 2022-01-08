package com.bankaccount.backend.service;

import java.util.Date;
import java.util.Optional;

import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
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

    public Account read(Long id) {
		Optional<Account> optional = accountRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return  null;
	}

	public Account create(Account account) {
        bankClientRepository.save(account.getClient());
		return accountRepository.save(account);
	}
    
    public Account saveMoney(Account account, float amount){
        account.addToBalance(amount);
        Operation operation = new Operation();
        operation.setOperationName("DEPOSIT");
        operation.setAmount(amount);
        operation.setBalance(account.getBalance());
        operation.setDate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        return accountRepository.save(account);
    }

    public Account withdrawMoney(Account account, float amount){
        account.retrieveFromBalance(amount);
        Operation operation = new Operation();
        operation.setOperationName("WITHDRAWAL");
        operation.setAmount(-amount);
        operation.setBalance(account.getBalance());
        operation.setDate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        return accountRepository.save(account);
    }

    public Account update(Long id, Account update) {
        Account account = read(id);
		account.setBalance(update.getBalance());
		return accountRepository.save(account);
    }

    public Iterable<Account> list() {
        return accountRepository.findAll();
    }


    

}
