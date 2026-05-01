package com.attendly.attendly_backend.modules.attendance.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAttendanceRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Programme ID is required")
    private Long programmeId;

    @NotNull(message = "Time is required")
    private LocalDateTime time;

    @NotBlank(message = "Status is required")
    private String status;
}
