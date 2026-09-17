package com.example.backend.service;

import com.example.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
    );

    @Test
    void generatesAndValidatesUserId() {
        User user = new User(42, "Test User", "test@example.com", LocalDateTime.now());

        String token = jwtService.generateToken(user);

        assertEquals(42, jwtService.extractUserIdFromHeader("Bearer " + token));
    }

    @Test
    void rejectsInvalidToken() {
        assertThrows(
                ResponseStatusException.class,
                () -> jwtService.extractUserIdFromHeader("Bearer not-a-token")
        );
    }
}
