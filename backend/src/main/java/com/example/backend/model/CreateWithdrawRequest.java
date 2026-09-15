package com.example.backend.model;

public class CreateWithdrawRequest {

    private int id;
    private double amount;

    public CreateWithdrawRequest() {
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
