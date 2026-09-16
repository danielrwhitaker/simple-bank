package com.example.backend.model;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private String name;
    private BigDecimal balance;

    public CreateAccountRequest() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}