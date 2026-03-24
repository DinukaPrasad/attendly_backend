package com.attendly.attendly_backend.modules.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.user.dto.AuthResponse;
import com.attendly.attendly_backend.modules.user.dto.LoginRequest;
import com.attendly.attendly_backend.modules.user.model.User;
import com.attendly.attendly_backend.modules.user.repo.UserRepository;
import com.attendly.attendly_backend.security.JwtTokenProvider;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(LoginRequest loginRequest) {

        if (loginRequest.getEmail().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email and password must not be empty");
        }

        try {
            User loginUser = userRepository.findByEmail(loginRequest.getEmail());
            if (loginUser != null && loginUser.getPassword().equals(loginRequest.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", loginUser.getId());
                claims.put("role", loginUser.getRole());
                claims.put("name", loginUser.getName());

                String token = jwtTokenProvider.generateToken(loginUser.getEmail(), claims);
                return new AuthResponse(token, "Login successful!", loginUser.getRole());
            } else {
                throw new IllegalArgumentException("Invalid email or password");
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during login: " + e.getMessage());
        }

    }

}