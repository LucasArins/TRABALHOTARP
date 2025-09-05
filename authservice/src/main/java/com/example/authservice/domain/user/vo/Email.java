package com.example.authservice.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Email {

    @jakarta.validation.constraints.Email(message = "Email invalido")
    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public Email(String value) {
        this.value = normalize(value);

        if (this.value == null || this.value.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser em branco");
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}
