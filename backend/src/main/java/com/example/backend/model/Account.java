package com.example.backend.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

//This is the Account class, which is defining Accounts to be built in accountservice
@Entity
@Table(name="accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type;
    private BigDecimal balance;

    public Account() {
    }
    /*
    public Account(User user, int id, String name, String type, BigDecimal balance) {
        this.user = user;
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }*/

    public Account(User user, String type, BigDecimal balance) {
        this.user = user;
        this.type = type;
        this.balance = balance;
    }
    /*
    public Account(String name, String type, BigDecimal balance) {
        this.name = name;
        this.type = type;
        this.balance = balance;
    }*/

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
    
    public String getType() {return type; }

    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", user id=" + user.getId() + ", type=" + type + ", balance=" + balance + "]";
    }
}
