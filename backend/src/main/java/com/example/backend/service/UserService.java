package com.example.backend.service;
//this AccountService is used to create Accounts with a hashmap
//hash map because no database is used yet
import com.example.backend.model.UpdateUserRequest;
import com.example.backend.model.User;
import com.example.backend.model.Account;
import com.example.backend.model.Transaction;
import com.example.backend.repository.AccountRepository;
import com.example.backend.repository.TransactionRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    public UserService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public User createUser(String name, String email) {

        LocalDateTime timestamp = LocalDateTime.now();
        User user = new User(name, email, timestamp);
        user = userRepository.save(user);

        return user;
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
