package com.example.backend.model;

import java.math.BigDecimal;

public class CreateUserRequest {

    private String name;
    private String email;

    public CreateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}