package com.attendly.attendly_backend.modules.notification.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationResponse {

    private final Long id;
    private final Long senderId;
    private final String senderName;
    private final Long recipientId;
    private final String recipientName;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime datetime;

    private final String status;
}
