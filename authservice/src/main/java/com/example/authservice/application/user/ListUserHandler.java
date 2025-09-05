package com.example.authservice.application.user;

import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.interfaces.rest.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListUserHandler {
    private final UserRepository userRepository;

    public Page<UserResponse> handle(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);

        return page.map(u -> new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail().getValue(),
                u.getRole().getValue().name()
        ));
    }
}
