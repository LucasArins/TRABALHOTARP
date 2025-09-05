package com.example.authservice.application.user;

import com.example.authservice.application.ports.PasswordHasher;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.domain.user.vo.RoleType;
import com.example.authservice.interfaces.rest.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserResponse handle(String name, String email, String password) {
        Email emailObj = Email.of(email);
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        String hashedPassword = passwordHasher.hash(password);
        User user = new User(name, emailObj, RoleType.CUSTOMER, hashedPassword);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail().getValue(),
                savedUser.getRole().getValue().name()
        );
    }
}
