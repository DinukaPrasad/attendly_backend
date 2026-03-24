package com.attendly.attendly_backend.modules.session.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendly.attendly_backend.modules.session.dto.SessionRequest;
import com.attendly.attendly_backend.modules.session.dto.SessionResponse;
import com.attendly.attendly_backend.modules.session.service.SessionService;
import com.attendly.attendly_backend.utility.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getAllSessions() {
        List<SessionResponse> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(ApiResponse.success("Sessions retrieved successfully", sessions));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SessionResponse>> createSession(
            @Valid @RequestBody SessionRequest sessionRequest) {
        SessionResponse session = sessionService.createSession(sessionRequest);
        return ResponseEntity.ok(ApiResponse.success("Session created successfully", session));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> updateSession(@PathVariable Long id,
            @Valid @RequestBody SessionRequest sessionRequest) {
        SessionResponse session = sessionService.updateSessionById(id, sessionRequest);
        return ResponseEntity.ok(ApiResponse.success("Session updated successfully", session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Long id) {
        sessionService.deleteSessionById(id);
        return ResponseEntity.ok(ApiResponse.success("Session deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> updateSessionById(@PathVariable Long id,
            @Valid @RequestBody SessionRequest sessionRequest) {
        SessionResponse session = sessionService.updateSessionById(id, sessionRequest);
        return ResponseEntity.ok(ApiResponse.success("Session updated successfully", session));
    }

}
