package com.attendly.attendly_backend.modules.programme.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.programme.dto.CreateProgrammeRequest;
import com.attendly.attendly_backend.modules.programme.dto.ProgrammeResponse;
import com.attendly.attendly_backend.modules.programme.model.Programme;
import com.attendly.attendly_backend.modules.programme.repo.ProgrammeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;

    public List<ProgrammeResponse> getAllProgrammes() {

        log.info("Starting fetching all programmes");
        List<ProgrammeResponse> programmes = programmeRepository.findAll().stream()
                .map(this::convertProgrammeToProgrammeResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching all programmes, total programmes found: {}", programmes.size());
        return programmes;
    }

    public ProgrammeResponse createProgramme(CreateProgrammeRequest request) {
        if (request == null) {
            log.error("Failed to create programme: CreateProgrammeRequest is null");
            throw new IllegalArgumentException("Programme data must not be null");
        }
        log.info("Creating programme with data: {}", request);

        Programme programme = new Programme();
        programme.setCode(request.getCode());
        programme.setName(request.getTitle());
        programme.setDescription(request.getDescription());

        programme = programmeRepository.save(programme);

        log.info("Programme created successfully with id: {}", programme.getId());
        return convertProgrammeToProgrammeResponse(programme);
    }

    public ProgrammeResponse updateProgrammeById(Long id, CreateProgrammeRequest request) {

        if (request == null) {
            log.error("Failed to update programme: CreateProgrammeRequest is null");
            throw new IllegalArgumentException("Programme data must not be null");
        }
        log.info("Updating programme with id: {}", id);

        Programme programme = programmeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Programme not found with id: " + id));

        boolean hasUpdates = false;

        if (isValid(request.getCode())) {
            programme.setCode(request.getCode());
            hasUpdates = true;
        }
        if (isValid(request.getTitle())) {
            programme.setName(request.getTitle());
            hasUpdates = true;
        }
        if (isValid(request.getDescription())) {
            programme.setDescription(request.getDescription());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }

        programme = programmeRepository.save(programme);

        log.info("Programme updated successfully with id: {}", programme.getId());
        return convertProgrammeToProgrammeResponse(programme);
    }

    public void deleteProgrammeById(Long id) {
        log.info("Deleting programme with id: {}", id);
        if (!programmeRepository.existsById(id)) {
            log.error("Failed to delete programme: Programme not found with id: {}", id);
            throw new NoSuchElementException("Programme not found with id: " + id);
        }

        programmeRepository.deleteById(id);
        log.info("Programme deleted successfully with id: {}", id);
    }

    public ProgrammeResponse convertProgrammeToProgrammeResponse(Programme programme) {
        if (programme == null) {
            throw new IllegalArgumentException("Programme must not be null");
        }
        log.info("Converting programme to response with id: {}", programme.getId());

        return ProgrammeResponse.builder()
                .title(programme.getName())
                .description(programme.getDescription())
                .code(programme.getCode())
                .build();
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

}
