package com.example.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

/*This is the User class, which is defining Users. Belongs above accounts
and transactions
CREATE TABLE users (
        user_id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(100),
        email VARCHAR(100) UNIQUE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);*/

@Entity
@Table(name="users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    private String name;


    private String email;
    private LocalDateTime timestamp;

    public User() {
    }

    public User(int id, String name, String email, LocalDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.timestamp = timestamp;
    }

    public User(String name, String email, LocalDateTime timestamp) {
        this.name = name;
        this.email = email;
        this.timestamp = timestamp;
    }

    //necessary for now
//    public User(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }

    /* not yet
    public User(Account account, String type, BigDecimal amount, LocalDateTime timestamp) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }*/

    public int getId() {
        return id;
    }


    public List<Account> getAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", name=" + name + ", email=" + email + ", timestamp=" + timestamp + "]";
    }
}
