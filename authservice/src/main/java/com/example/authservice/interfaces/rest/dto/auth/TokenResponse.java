package com.example.authservice.interfaces.rest.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
