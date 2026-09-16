package com.example.backend.model;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private String type;
    private BigDecimal balance;

    public CreateAccountRequest() {
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}