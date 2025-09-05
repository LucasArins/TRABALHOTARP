package com.example.authservice.interfaces.rest;

import com.example.authservice.application.auth.PasswordLoginHandler;
import com.example.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordLoginHandler passwordLoginHandler;

    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse token = passwordLoginHandler.handle(
            request.email(),
            request.password()
        );

        return ResponseEntity.ok(token);
    }
}
