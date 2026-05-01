package com.attendly.attendly_backend.modules.attendance.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AttendanceResponse {

    private final Long id;
    private final Long userId;
    private final String userName;
    private final Long programmeId;
    private final String programmeName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime time;

    private final String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;
}
