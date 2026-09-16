package com.example.backend.model;

import java.math.BigDecimal;

public class CreateWithdrawRequest {

    private int id;
    private BigDecimal amount;

    public CreateWithdrawRequest() {
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
