package com.bankaccount.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import com.bankaccount.backend.entity.Account;
import com.bankaccount.backend.entity.Operation;
import com.bankaccount.backend.exception.AccountAlreadyCreatedException;
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
        return optional.orElseThrow(() -> new AccountNotFoundException("Account with id :" + id + " is not found."));
	}

	public Account create(Account account) throws AccountAlreadyCreatedException{
        bankClientRepository.save(account.getClient());
		return accountRepository.save(account);
	}

    public synchronized float getBalance(Long id) throws AccountNotFoundException{
        if (!accountRepository.findById(id).isPresent()){
            throw new AccountNotFoundException("Account with id: "+ id + " is not found.");
        }
        List<Operation> operations = operationRepository.findByAccountId(id);
        LocalDateTime now = LocalDateTime.now();
        float result = 0;
        for(Operation operation : operations){
            if(operation.getLocalDateTime().isBefore(now)){
                result += operation.getAmount();
            }
        }
        return result;
    }
    
    public Operation saveMoney(Account account, float amount) throws IllegalOperationException{
        if (amount < 0){
            throw new IllegalOperationException("The amount should be positive.");
        }
        Operation operation = new Operation();
        operation.setOperationName("DEPOSIT");
        operation.setAmount(amount);
        operation.setLocalDateTime(LocalDateTime.now());
        operation.setAccount(account);
        return operationRepository.save(operation);
    }

    public Operation withdrawMoney(Account account, float amount) throws IllegalOperationException, AccountNotFoundException{
        if (amount < 0){
            throw new IllegalOperationException("The amount should be positive.");
        }

        if (amount > getBalance(account.getId())){
            throw new IllegalOperationException("Your account balance is lower than the amount desired.");
        }
        
        Operation operation = new Operation();
        operation.setOperationName("WITHDRAWAL");
        operation.setAmount(-amount);
        operation.setLocalDateTime(LocalDateTime.now());
        operation.setAccount(account);
        return operationRepository.save(operation);
    }

    public Account update(Long id, Account update) throws AccountNotFoundException {
        Account account = read(id);
		return accountRepository.save(account);
    }

    public List<Account> list() {
        return accountRepository.findAll();
    }

    public void deleteAll(){
        accountRepository.deleteAll();
    }
}
