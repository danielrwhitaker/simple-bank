package com.example.backend.controller;

import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import com.example.backend.model.CreateAccountRequest;
import com.example.backend.model.CreateWithdrawRequest;
import com.example.backend.model.CreateDepositRequest;
import com.example.backend.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Create part of CRUD
    @PostMapping
    public Account createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request.getName(), request.getBalance());
    }

    //Update part of CRUD
    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable int id, @RequestBody CreateWithdrawRequest request) {
        return accountService.withdraw(id, request.getAmount());
    }

    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable int id, @RequestBody CreateDepositRequest request) {
        return accountService.deposit(id, request.getAmount());
    }


    //Read part of CRUD
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable int id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransaction(@PathVariable int id) {
        return accountService.getTransaction(id);
    }
}
