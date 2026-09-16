package com.example.backend.service;
//this AccountService is used to create Accounts with a hashmap
//hash map because no database is used yet
import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.model.UpdateUserRequest;
import com.example.backend.model.User;
import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(RegistrationRequest request) {

        LocalDateTime timestamp = LocalDateTime.now();

        User user = new User(request.getName(), request.getEmail(), timestamp);

        String passwordHash = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(passwordHash);

        user = userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User getUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateUser(int id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(request.getName() != null) {
            user.setName(request.getName());
        }

        if(request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.deleteById(id);
    }
}
