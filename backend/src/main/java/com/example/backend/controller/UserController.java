package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.UpdateUserRequest;
import com.example.backend.model.User;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserNotFoundException;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }

    //Create part of CRUD
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody RegistrationRequest request) {
        return userService.createUser(request);
    }

    //Read part of CRUD
    @GetMapping("/{id}")
    public UserResponse getUser(
            @PathVariable int id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        requireCurrentUser(id, authorizationHeader);
        return userService.getUser(id);
    }

    //Update part of CRUD
    @PatchMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable int id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody UpdateUserRequest request) {
        requireCurrentUser(id, authorizationHeader);
        return userService.updateUser(id, request);
    }

    private void requireCurrentUser(int id, String authorizationHeader) {
        if (jwtService.extractUserIdFromHeader(authorizationHeader) != id) {
            throw new UserNotFoundException(id);
        }
    }
}

