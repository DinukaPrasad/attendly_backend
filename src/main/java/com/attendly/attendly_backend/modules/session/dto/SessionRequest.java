package com.attendly.attendly_backend.modules.session.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode
public class SessionRequest {
    @NotNull(message = "Programme ID is required")
    private Long programmeId;

    @NotBlank(message = "Module is required")
    private String module;

    @NotBlank(message = "Lecturer is required")
    private String lecturer;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotBlank(message = "Status is required")
    private String sessionStatus;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Venue is required")
    private String venue;
}