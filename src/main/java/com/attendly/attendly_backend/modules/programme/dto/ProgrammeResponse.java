package com.attendly.attendly_backend.modules.programme.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProgrammeResponse {
    private final String name;
    private final String description;
    private final String code;

}
