package com.attendly.attendly_backend.modules.session.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionResponse {
    private final String module;
    private final String lecturer;
    private final String title;
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime endTime;
    private final String status;
    private final String code;
    private final String venue;
}
