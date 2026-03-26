package com.attendly.attendly_backend.modules.session.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode
public class SessionRequest {
    @NotBlank(message = "Module is required")
    private String module;

    @NotBlank(message = "Lecturer is required")
    private String lecturer;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotBlank(message = "End time is required")
    private String endTime;

    @NotBlank(message = "Status is required zzzzzz ")
    private String sessionStatus;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Venue is required")
    private String venue;
}