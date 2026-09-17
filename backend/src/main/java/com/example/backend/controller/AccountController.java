package com.example.backend.controller;

import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import com.example.backend.model.CreateAccountRequest;
import com.example.backend.model.CreateWithdrawRequest;
import com.example.backend.model.CreateDepositRequest;
import com.example.backend.service.AccountService;
import com.example.backend.service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;


    public AccountController(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    //Create part of CRUD
    @PostMapping("/users/{userId}/accounts")
    public Account createAccount(@PathVariable int userId, @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(userId, request.getType(), request.getBalance());
    }

    //Update part of CRUD
    @PostMapping("/accounts/{id}/withdraw")
    public Account withdraw(@PathVariable int id, @RequestHeader(value = "Authorization", required = false)
    String authorizationHeader, @RequestBody CreateWithdrawRequest request) {

        int userId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return accountService.withdraw(
                id,
                userId,
                request.getAmount()
        );
    }

    @PostMapping("/accounts/{id}/deposit")
    public Account deposit(@PathVariable int id, @RequestHeader(value = "Authorization", required = false)
    String authorizationHeader, @RequestBody CreateDepositRequest request) {
        int userId = jwtService.extractUserIdFromHeader(authorizationHeader);

        return accountService.deposit(
                id,
                userId,
                request.getAmount()
        );
    }


    //Read part of CRUD
    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable int id, @RequestHeader(value = "Authorization", required = false)
    String authorizationHeader) {

        int userId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return accountService.getOwnedAccount(id, userId);
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransaction(@PathVariable int id, @RequestHeader(value = "Authorization", required = false)
    String authorizationHeader) {
        int userId =
                jwtService.extractUserIdFromHeader(authorizationHeader);

        return accountService.getTransaction(id, userId);
    }
}
