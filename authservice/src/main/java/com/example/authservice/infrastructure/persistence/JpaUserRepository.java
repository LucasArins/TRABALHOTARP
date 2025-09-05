package com.example.authservice.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserJpa jpa;

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail_value(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail_value(email);
    }

    @Override
    public User save(User user) {
        return jpa.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpa.findAll(pageable);
    }
    
}
