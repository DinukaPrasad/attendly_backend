package com.attendly.attendly_backend.modules.notification.repo;

import java.util.List;

import com.attendly.attendly_backend.modules.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientId(Long recipientId);

    List<Notification> findBySenderId(Long senderId);

    List<Notification> findByRecipientIdAndStatus(Long recipientId, String status);
}
