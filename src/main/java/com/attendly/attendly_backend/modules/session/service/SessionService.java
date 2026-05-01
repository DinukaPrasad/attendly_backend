package com.attendly.attendly_backend.modules.session.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.programme.model.Programme;
import com.attendly.attendly_backend.modules.programme.repo.ProgrammeRepository;
import com.attendly.attendly_backend.modules.session.dto.*;
import com.attendly.attendly_backend.modules.session.model.Session;
import com.attendly.attendly_backend.modules.session.repo.SessionRepository;
import com.attendly.attendly_backend.utility.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ProgrammeRepository programmeRepository;

    public List<SessionResponse> getAllSessions() {

        log.info("Starting Fetching all sessions");
        List<SessionResponse> sessions = sessionRepository.findAll().stream()
                .map(this::convertSessionToSessionResponse)
                .collect(Collectors.toList());
        log.info("Finished Fetching all sessions, total sessions found: {}", sessions.size());
        return sessions;
    }

    public ApiResponse<SessionResponse> createSession(SessionRequest request) {
        if (request == null) {
            log.error("Failed to create session: SessionRequest is null");
            throw new IllegalArgumentException("Session data must not be null");
        }
        log.info("Creating session with data: {}", request);

        Programme programme = programmeRepository.findById(request.getProgrammeId())
                .orElse(null);
        if (programme == null) {
            log.error("Failed to create session: Programme not found with id: {}", request.getProgrammeId());
            return ApiResponse.error("Programme not found with id: " + request.getProgrammeId());
        }

        Session session = new Session();
        session.setProgramme(programme);
        session.setModule(request.getModule());
        session.setLecturer(request.getLecturer());
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setSessionStatus(request.getSessionStatus());
        session.setCode(request.getCode());
        session.setVenue(request.getVenue());
        session.setAttendanceStatus(false);

        session = sessionRepository.save(session);

        log.info("Session created successfully with id: {}", session.getId());
        return ApiResponse.success("Session created successfully", convertSessionToSessionResponse(session));
    }

    public SessionResponse updateSessionById(Long id, SessionRequest request) {

        if (request == null) {
            log.error("Failed to update session: SessionRequest is null");
            throw new IllegalArgumentException("Session data must not be null");
        }
        log.info("Updating session with id: {}", id);

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Session not found with id: " + id));

        boolean hasUpdates = false;

        if (isValid(request.getModule())) {
            session.setModule(request.getModule());
            hasUpdates = true;
        }
        if (isValid(request.getLecturer())) {
            session.setLecturer(request.getLecturer());
            hasUpdates = true;
        }
        if (isValid(request.getTitle())) {
            session.setTitle(request.getTitle());
            hasUpdates = true;
        }
        if (isValid(request.getDescription())) {
            session.setDescription(request.getDescription());
            hasUpdates = true;
        }
        if (request.getStartTime() != null) {
            session.setStartTime(request.getStartTime());
            hasUpdates = true;
        }
        if (request.getEndTime() != null) {
            session.setEndTime(request.getEndTime());
            hasUpdates = true;
        }
        if (isValid(request.getSessionStatus())) {
            session.setSessionStatus(request.getSessionStatus());
            hasUpdates = true;
        }
        if (isValid(request.getCode())) {
            session.setCode(request.getCode());
            hasUpdates = true;
        }
        if (isValid(request.getVenue())) {
            session.setVenue(request.getVenue());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }

        log.info("Session updated successfully with id: {}", session.getId());
        return convertSessionToSessionResponse(session);
    }

    public void deleteSessionById(Long id) {
        log.info("Deleting session with id: {}", id);
        if (!sessionRepository.existsById(id)) {
            log.error("Failed to delete session: Session not found with id: {}", id);
            throw new NoSuchElementException("Session not found with id: " + id);
        }

        sessionRepository.deleteById(id);
        log.info("Session deleted successfully with id: {}", id);
    }
    // ! Utility methods

    public SessionResponse convertSessionToSessionResponse(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null");
        }
        log.info("Converting session to response with id: {}", session.getId());

        return SessionResponse.builder()
                .module(session.getModule())
                .lecturer(session.getLecturer())
                .title(session.getTitle())
                .description(session.getDescription())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .status(session.getSessionStatus())
                .code(session.getCode())
                .venue(session.getVenue())
                .build();
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

}
