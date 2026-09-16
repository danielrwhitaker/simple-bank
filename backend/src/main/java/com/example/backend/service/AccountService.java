package com.example.backend.service;
//this AccountService is used to create Accounts with a hashmap
//hash map because no database is used yet
import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final Map<Integer, List<Transaction>> transactions = new HashMap<>();
    private int nextAccountId = 1;
    private int nextTransactionId = 1;

    public Account createAccount(String name, BigDecimal balance) {
        if(balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        int accountId = nextAccountId++;

        Account account = new Account(accountId, name, balance);
        accounts.put(account.getId(), account);

        transactions.put(account.getId(), new ArrayList<>());

        if(balance.compareTo(BigDecimal.ZERO) != 0) {
            int transactionId = nextTransactionId++;
            String type = "DEPOSIT";
            LocalDateTime timestamp = LocalDateTime.now();
            Transaction transaction = new Transaction(transactionId, accountId, type, balance, timestamp);

            transactions.get(accountId).addFirst(transaction);
        }

        return account;
    }

    public Account getAccount(int id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public List<Transaction> getTransaction(int id) {
        if (!accounts.containsKey(id)) {
            throw new AccountNotFoundException(id);
        }
        return transactions.get(id);
    }

    public Account withdraw(int id, BigDecimal amount) {
        Account account = accounts.get(id);

        if (account == null) {
            throw new AccountNotFoundException(id);
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot withdraw amount <= $0");
        }

        if(account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        int transactionId = nextTransactionId++;
        String type = "WITHDRAWAL";
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction = new Transaction(transactionId, id, type, amount, timestamp);

        account.setBalance(account.getBalance().subtract(amount));
        transactions.get(id).addFirst(transaction);

        return account;
    }

    public Account deposit(int id, BigDecimal amount) {
        Account account = accounts.get(id);

        if (account == null) {
            throw new AccountNotFoundException(id);
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot deposit amount <= $0");
        }

        int transactionId = nextTransactionId++;
        String type = "DEPOSIT";
        LocalDateTime timestamp = LocalDateTime.now();
        Transaction transaction = new Transaction(transactionId, id, type, amount, timestamp);

        account.setBalance(account.getBalance().add(amount));
        transactions.get(id).addFirst(transaction);

        return account;
    }
}
