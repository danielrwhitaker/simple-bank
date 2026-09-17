package com.example.backend.service;

import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.time.Instant;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;


    public int extractUserIdFromHeader(String authorizationHeader) {
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing or invalid authorization header"
            );
        }

        String token = authorizationHeader.substring(7);

        try {
            return extractUserId(token);
        } catch (JwtException | NumberFormatException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token"
            );
        }
    }

    public JwtService(@Value("${jwt.secret}") String secret) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofHours(1))))
                .signWith(signingKey)
                .compact();
    }

    public int extractUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Integer.parseInt(subject);
    }
}
