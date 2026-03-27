package com.attendly.attendly_backend.modules.programme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode
public class CreateProgrammeRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Code is required")
    private String code;
}
