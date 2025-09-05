package com.example.authservice.domain.user.vo;

import lombok.Getter;

@Getter
public enum RoleType {
    CUSTOMER(1),
    WAITER(2),
    CHEF(3),
    ADMIN(4);

    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean covers(RoleType other) {
        return this.level >= other.level;
    }
}
