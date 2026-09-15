package com.example.backend.controller;

import com.example.backend.model.Account;
import com.example.backend.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Create part of CRUD
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account.getName(), account.getBalance());
    }
    //Read part of CRUD
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable int id) {
        return accountService.getAccount(id);
    }
}
