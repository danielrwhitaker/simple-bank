package com.example.backend.model;

public class CreateAccountRequest {

    private String name;
    private double balance;

    public CreateAccountRequest() {
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}