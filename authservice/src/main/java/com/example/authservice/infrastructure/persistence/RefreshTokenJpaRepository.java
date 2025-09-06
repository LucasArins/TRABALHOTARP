package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, UUID> {

    @Query("select t from RefreshToken t where t.tokenHash.value = :hash and t.revoked = false and t.expiresAt > :now")
    Optional<RefreshToken> findActiveByHash(String hash, Instant now);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true, t.revokedAt = :when where t.id = :id and t.revoked = false")
    int revoke(UUID id, Instant when);
}

