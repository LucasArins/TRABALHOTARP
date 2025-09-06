package com.example.authservice.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TokenHash {

    @Column(name = "token_hash", nullable = false, unique = true, length = 128)
    private String value;

    private TokenHash(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Token hash must not be blank");
        }
        this.value = value;
    }

    public static TokenHash of(String value) {
        return new TokenHash(value);
    }
}

