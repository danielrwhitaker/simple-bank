package com.example.backend.model;

import java.math.BigDecimal;

public class UpdateUserRequest {
    private String name;
    private String email;

    public UpdateUserRequest() {
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