package com.example.authservice.interfaces.rest.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordLoginRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {}
