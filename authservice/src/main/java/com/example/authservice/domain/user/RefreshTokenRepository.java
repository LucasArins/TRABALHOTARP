package com.example.authservice.domain.user;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findActiveByHash(String hash, Instant now);
    void deleteById(UUID id);
    void revoke(UUID id, Instant when);
}

