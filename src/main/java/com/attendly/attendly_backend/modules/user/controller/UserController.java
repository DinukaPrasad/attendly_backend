package com.attendly.attendly_backend.modules.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.attendly.attendly_backend.modules.user.dto.UserResponse;
import com.attendly.attendly_backend.modules.user.dto.CreateUserRequest;
import com.attendly.attendly_backend.modules.user.dto.UpdateUserRequest;
import com.attendly.attendly_backend.modules.user.service.UserService;
import com.attendly.attendly_backend.utility.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", user));
    }

    /**
     * Get all users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get current user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@PathVariable Long id) {
        UserResponse user = userService.getCurrentUser(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    /**
     * Update user by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUserById(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        userService.updateUserById(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully"));
    }
}
