package com.example.authservice.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authservice.domain.user.User;

public interface SpringDataUserJpa extends JpaRepository<User, UUID>{
    Optional<User> findByEmail_value(String email);
    boolean existsByEmail_value(String email);
}
