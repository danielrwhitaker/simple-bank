package com.example.backend.service;
//this AccountService is used to create Accounts with a hashmap
//hash map because no database is used yet
import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccount(String name, BigDecimal balance) {
        if(balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Account account = new Account(name, balance);
        account = accountRepository.save(account);

        //shouldn't be necessary
        //int accountId = account.getId();



        if(balance.compareTo(BigDecimal.ZERO) != 0) {
            String type = "DEPOSIT";
            LocalDateTime timestamp = LocalDateTime.now();
            Transaction transaction =
                    new Transaction(account, type, balance, timestamp);

            transactionRepository.save(transaction);
        }

        return account;
    }

    public Account getAccount(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public List<Transaction> getTransaction(int id) {
        getAccount(id);

        return transactionRepository.findByAccountIdOrderByTimestampDesc(id);
    }

    @Transactional
    public Account withdraw(int id, BigDecimal amount) {
        Account account = getAccount(id);

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot withdraw amount <= $0");
        }

        if(account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        String type = "WITHDRAWAL";
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction =
                new Transaction(account, type, amount, timestamp);

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return account;
    }

    @Transactional
    public Account deposit(int id, BigDecimal amount) {
        Account account = getAccount(id);

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot deposit amount <= $0");
        }


        String type = "DEPOSIT";
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction =
                new Transaction(account, type, amount, timestamp);

        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return account;
    }
}
