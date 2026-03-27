package com.attendly.attendly_backend.modules.session.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionResponse {
    private final String module;
    private final String lecturer;
    private final String title;
    private final String description;
    private final String startTime;
    private final String endTime;
    private final String status;
    private final String code;
    private final String venue;
}
