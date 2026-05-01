package com.attendly.attendly_backend.modules.notification.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.notification.dto.CreateNotificationRequest;
import com.attendly.attendly_backend.modules.notification.dto.NotificationResponse;
import com.attendly.attendly_backend.modules.notification.model.Notification;
import com.attendly.attendly_backend.modules.notification.repo.NotificationRepository;
import com.attendly.attendly_backend.modules.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationResponse createNotification(CreateNotificationRequest request) {
        if (request == null) {
            log.error("Failed to create notification: CreateNotificationRequest is null");
            throw new IllegalArgumentException("Notification data must not be null");
        }

        log.info("Validating sender with id: {}", request.getSenderId());
        var sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NoSuchElementException("Sender not found with id: " + request.getSenderId()));

        log.info("Validating recipient with id: {}", request.getRecipientId());
        var recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(
                        () -> new NoSuchElementException("Recipient not found with id: " + request.getRecipientId()));

        log.info("Creating notification from sender: {} to recipient: {}", request.getSenderId(),
                request.getRecipientId());

        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setContent(request.getContent());
        notification.setStatus("UNREAD");

        notification = notificationRepository.save(notification);

        log.info("Notification created successfully with id: {}", notification.getId());
        return convertNotificationToNotificationResponse(notification);
    }

    public List<NotificationResponse> getAllNotifications() {
        log.info("Starting fetching all notifications");
        List<NotificationResponse> notifications = notificationRepository.findAll().stream()
                .map(this::convertNotificationToNotificationResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching all notifications, total notifications found: {}", notifications.size());
        return notifications;
    }

    public NotificationResponse getNotificationById(Long id) {
        log.info("Fetching notification with id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification not found with id: " + id));
        return convertNotificationToNotificationResponse(notification);
    }

    public List<NotificationResponse> getNotificationsByRecipientId(Long recipientId) {
        log.info("Fetching notifications for recipient with id: {}", recipientId);

        if (!userRepository.existsById(recipientId)) {
            log.error("Recipient not found with id: {}", recipientId);
            throw new NoSuchElementException("Recipient not found with id: " + recipientId);
        }

        List<NotificationResponse> notifications = notificationRepository.findByRecipientId(recipientId).stream()
                .map(this::convertNotificationToNotificationResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching notifications for recipient, total notifications found: {}", notifications.size());
        return notifications;
    }

    public List<NotificationResponse> getNotificationsBySenderId(Long senderId) {
        log.info("Fetching notifications from sender with id: {}", senderId);

        if (!userRepository.existsById(senderId)) {
            log.error("Sender not found with id: {}", senderId);
            throw new NoSuchElementException("Sender not found with id: " + senderId);
        }

        List<NotificationResponse> notifications = notificationRepository.findBySenderId(senderId).stream()
                .map(this::convertNotificationToNotificationResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching notifications from sender, total notifications found: {}", notifications.size());
        return notifications;
    }

    public List<NotificationResponse> getUnreadByRecipientId(Long recipientId) {
        log.info("Fetching unread notifications for recipient with id: {}", recipientId);

        if (!userRepository.existsById(recipientId)) {
            log.error("Recipient not found with id: {}", recipientId);
            throw new NoSuchElementException("Recipient not found with id: " + recipientId);
        }

        List<NotificationResponse> notifications = notificationRepository
                .findByRecipientIdAndStatus(recipientId, "UNREAD").stream()
                .map(this::convertNotificationToNotificationResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching unread notifications for recipient, total notifications found: {}",
                notifications.size());
        return notifications;
    }

    public NotificationResponse markAsRead(Long id) {
        log.info("Marking notification with id: {} as read", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification not found with id: " + id));

        notification.setStatus("READ");
        notification = notificationRepository.save(notification);

        log.info("Notification marked as read successfully with id: {}", notification.getId());
        return convertNotificationToNotificationResponse(notification);
    }

    public void deleteNotification(Long id) {
        log.info("Deleting notification with id: {}", id);
        if (!notificationRepository.existsById(id)) {
            log.error("Failed to delete notification: Notification not found with id: {}", id);
            throw new NoSuchElementException("Notification not found with id: " + id);
        }

        notificationRepository.deleteById(id);
        log.info("Notification deleted successfully with id: {}", id);
    }

    private NotificationResponse convertNotificationToNotificationResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .senderId(notification.getSender().getId())
                .senderName(notification.getSender().getName())
                .recipientId(notification.getRecipient().getId())
                .recipientName(notification.getRecipient().getName())
                .content(notification.getContent())
                .datetime(notification.getDatetime())
                .status(notification.getStatus())
                .build();
    }
}
