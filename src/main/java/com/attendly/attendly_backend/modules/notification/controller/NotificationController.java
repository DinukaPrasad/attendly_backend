package com.attendly.attendly_backend.modules.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendly.attendly_backend.modules.notification.dto.CreateNotificationRequest;
import com.attendly.attendly_backend.modules.notification.dto.NotificationResponse;
import com.attendly.attendly_backend.modules.notification.service.NotificationService;
import com.attendly.attendly_backend.utility.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse notification = notificationService.createNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification sent successfully", notification));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved successfully", notifications));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(@PathVariable Long id) {
        NotificationResponse notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success("Notification retrieved successfully", notification));
    }

    @GetMapping("/recipient/{recipientId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByRecipientId(
            @PathVariable Long recipientId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByRecipientId(recipientId);
        return ResponseEntity
                .ok(ApiResponse.success("Notifications for recipient retrieved successfully", notifications));
    }

    @GetMapping("/sender/{senderId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsBySenderId(
            @PathVariable Long senderId) {
        List<NotificationResponse> notifications = notificationService.getNotificationsBySenderId(senderId);
        return ResponseEntity
                .ok(ApiResponse.success("Notifications from sender retrieved successfully", notifications));
    }

    @GetMapping("/unread/{recipientId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadByRecipientId(
            @PathVariable Long recipientId) {
        List<NotificationResponse> notifications = notificationService.getUnreadByRecipientId(recipientId);
        return ResponseEntity.ok(ApiResponse.success("Unread notifications retrieved successfully", notifications));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        NotificationResponse notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read successfully", notification));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully"));
    }
}
