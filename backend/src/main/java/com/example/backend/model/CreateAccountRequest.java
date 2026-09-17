package com.example.backend.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateAccountRequest {

    @Positive
    private int userId;

    @NotBlank
    private String accountType;

    @DecimalMin("0.0")
    private BigDecimal balance = BigDecimal.ZERO;

    public CreateAccountRequest() {
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
