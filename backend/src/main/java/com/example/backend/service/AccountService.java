package com.example.backend.service;
//this AccountService is used to create Accounts with a hashmap
//hash map because no database is used yet
import com.example.backend.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private int nextId = 1;

    public Account createAccount(String name, double balance) {
        int id = nextId++;
        Account account = new Account(id, name, balance);
        accounts.put(id, account);
        return account;
    }

     public Account getAccount(int id) {
        return accounts.get(id);
    }
}
