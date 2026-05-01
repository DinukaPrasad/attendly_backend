package com.attendly.attendly_backend.modules.programme.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendly.attendly_backend.modules.programme.dto.CreateProgrammeRequest;
import com.attendly.attendly_backend.modules.programme.dto.ProgrammeResponse;
import com.attendly.attendly_backend.modules.programme.service.ProgrammeService;
import com.attendly.attendly_backend.utility.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/programmes")
@RequiredArgsConstructor
public class ProgrammeController {

    private final ProgrammeService programmeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','LECTURER')")
    public ResponseEntity<ApiResponse<List<ProgrammeResponse>>> getAllProgrammes() {
        List<ProgrammeResponse> programmes = programmeService.getAllProgrammes();
        return ResponseEntity.ok(ApiResponse.success("Programmes retrieved successfully", programmes));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> createProgramme(
            @Valid @RequestBody CreateProgrammeRequest createProgrammeRequest) {
        ProgrammeResponse programme = programmeService.createProgramme(createProgrammeRequest);
        return ResponseEntity.ok(ApiResponse.success("Programme created successfully", programme));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> updateProgramme(@PathVariable Long id,
            @Valid @RequestBody CreateProgrammeRequest createProgrammeRequest) {
        ProgrammeResponse programme = programmeService.updateProgrammeById(id, createProgrammeRequest);
        return ResponseEntity.ok(ApiResponse.success("Programme updated successfully", programme));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProgramme(@PathVariable Long id) {
        programmeService.deleteProgrammeById(id);
        return ResponseEntity.ok(ApiResponse.success("Programme deleted successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<ProgrammeResponse>> updateProgrammeById(@PathVariable Long id,
            @Valid @RequestBody CreateProgrammeRequest createProgrammeRequest) {
        ProgrammeResponse programme = programmeService.updateProgrammeById(id, createProgrammeRequest);
        return ResponseEntity.ok(ApiResponse.success("Programme updated successfully", programme));
    }

}
