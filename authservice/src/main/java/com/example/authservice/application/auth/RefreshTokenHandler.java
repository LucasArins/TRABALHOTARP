package com.example.authservice.application.auth;

import com.example.authservice.application.ports.TokenService;
import com.example.authservice.domain.user.RefreshTokenRepository;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenHandler {
    private final RefreshTokenRepository refreshRepo;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private String sha256Base64Url(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Hash error", e);
        }
    }

    @Transactional
    public TokenResponse refresh(String rawRefresh) {
        String hash = sha256Base64Url(rawRefresh);
        var token = refreshRepo.findActiveByHash(hash, Instant.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalido"));

        UUID userId = token.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));

        // rotate: revoke current and issue a new one via TokenService
        refreshRepo.revoke(token.getId(), Instant.now());
        var pair = tokenService.issue(user);
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.expiresInSeconds());
    }

    @Transactional
    public void logout(String rawRefresh) {
        String hash = sha256Base64Url(rawRefresh);
        refreshRepo.findActiveByHash(hash, Instant.now())
                .ifPresent(rt -> refreshRepo.revoke(rt.getId(), Instant.now()));
    }
}

