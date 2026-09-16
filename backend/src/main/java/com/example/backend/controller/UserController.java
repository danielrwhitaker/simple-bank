package com.example.backend.controller;

import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.*;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Create part of CRUD
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody RegistrationRequest request) {
        return userService.createUser(request);
    }

    //Read part of CRUD
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    //Update part of CRUD
    @PatchMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }
}

