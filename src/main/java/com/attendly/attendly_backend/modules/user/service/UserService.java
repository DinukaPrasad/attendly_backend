package com.attendly.attendly_backend.modules.user.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.user.dto.CreateUserRequest;
import com.attendly.attendly_backend.modules.user.dto.UpdateUserRequest;
import com.attendly.attendly_backend.modules.user.dto.UserResponse;
import com.attendly.attendly_backend.modules.user.model.User;
import com.attendly.attendly_backend.modules.user.repo.UserRepository;
import com.attendly.attendly_backend.utility.ApiResponse;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getCurrentUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return convertUserToUserResponse(user);
    }

    public ApiResponse<UserResponse> createUser(CreateUserRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ApiResponse.error("Email already registered");
        }

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        User savedUser = userRepository.save(newUser);
        return ApiResponse.success("User created successfully", convertUserToUserResponse(savedUser));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserById(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        boolean hasUpdates = false;

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
            hasUpdates = true;
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
            hasUpdates = true;
        }
        if (request.getRole() != null && !request.getRole().isBlank()) {
            user.setRole(request.getRole());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }

        userRepository.save(user);
    }

    public UserResponse convertUserToUserResponse(User user) {

        return new UserResponse(user.getId(), user.getName(), user.getRole(), user.getEmail());
    }
}
