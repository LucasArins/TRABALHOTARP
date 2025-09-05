package com.example.authservice.domain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Role {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 15)
    private RoleType value;

    private Role(RoleType role) {
        if (role == null) {
            throw new IllegalArgumentException("Role obrigatória");
        }

        this.value = role;
    }

    public static Role of(RoleType role) {
        return new Role(role);
    }

    public boolean covers(RoleType other) {
        return this.value.covers(other);
    }
}
