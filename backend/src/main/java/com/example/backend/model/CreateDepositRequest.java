package com.example.backend.model;

public class CreateDepositRequest {

    private int id;
    private double amount;

    public CreateDepositRequest() {
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
