package com.example.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public Transaction() {
    }

    //should not need this, but keeping in case
    /*public Transaction(int id, Account account, String type, BigDecimal amount, LocalDateTime timestamp) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }*/

    public Transaction(Account account, String type, BigDecimal amount, LocalDateTime timestamp) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", account id=" + account.getId() + ", type=" + type + ", amount=" + amount + ", timestamp=" + timestamp + "]";
    }
}
