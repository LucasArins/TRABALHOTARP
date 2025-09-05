package com.example.authservice.interfaces.rest;

import com.example.authservice.application.user.GetUserByIdHandler;
import com.example.authservice.application.user.ListUserHandler;
import com.example.authservice.application.user.RegisterUserHandler;
import com.example.authservice.domain.user.User;
import com.example.authservice.interfaces.rest.dto.RegisterUserRequest;
import com.example.authservice.interfaces.rest.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final ListUserHandler listUserHandler;
    private final RegisterUserHandler registerUserHandler;
    private final GetUserByIdHandler getUserByIdHandler;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        Page<UserResponse> users = listUserHandler.handle(pageable);

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created = registerUserHandler.handle(
                request.name(),
                request.email(),
                request.password()
        );

        return ResponseEntity
                .created(URI.create("/users/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        UserResponse user = getUserByIdHandler.handle(id);

        return ResponseEntity.ok(user);
    }
}
