package com.example.authservice.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> findById(UUID id);
    Page<User> findAll(Pageable pageable);
}
