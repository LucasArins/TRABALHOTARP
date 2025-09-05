package com.example.authservice.application.ports;

public interface PasswordHasher {
    public String hash(String password);
    boolean matches(String password, String hashedPassword);
}
