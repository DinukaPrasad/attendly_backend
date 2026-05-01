package com.attendly.attendly_backend.modules.attendance.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendly.attendly_backend.modules.attendance.dto.AttendanceResponse;
import com.attendly.attendly_backend.modules.attendance.dto.CreateAttendanceRequest;
import com.attendly.attendly_backend.modules.attendance.model.Attendance;
import com.attendly.attendly_backend.modules.attendance.repo.AttendanceRepository;
import com.attendly.attendly_backend.modules.programme.repo.ProgrammeRepository;
import com.attendly.attendly_backend.modules.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ProgrammeRepository programmeRepository;

    public AttendanceResponse createAttendance(CreateAttendanceRequest request) {
        if (request == null) {
            log.error("Failed to create attendance: CreateAttendanceRequest is null");
            throw new IllegalArgumentException("Attendance data must not be null");
        }

        log.info("Validating user with id: {}", request.getUserId());
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + request.getUserId()));

        log.info("Validating programme with id: {}", request.getProgrammeId());
        var programme = programmeRepository.findById(request.getProgrammeId())
                .orElseThrow(
                        () -> new NoSuchElementException("Programme not found with id: " + request.getProgrammeId()));

        log.info("Creating attendance record for user: {} and programme: {}", request.getUserId(),
                request.getProgrammeId());

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setProgramme(programme);
        attendance.setTime(request.getTime());
        attendance.setStatus(request.getStatus());

        attendance = attendanceRepository.save(attendance);

        log.info("Attendance created successfully with id: {}", attendance.getId());
        return convertAttendanceToAttendanceResponse(attendance);
    }

    public List<AttendanceResponse> getAllAttendance() {
        log.info("Starting fetching all attendance records");
        List<AttendanceResponse> attendances = attendanceRepository.findAll().stream()
                .map(this::convertAttendanceToAttendanceResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching all attendance records, total records found: {}", attendances.size());
        return attendances;
    }

    public AttendanceResponse getAttendanceById(Long id) {
        log.info("Fetching attendance record with id: {}", id);
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Attendance not found with id: " + id));
        return convertAttendanceToAttendanceResponse(attendance);
    }

    public List<AttendanceResponse> getAttendanceByUserId(Long userId) {
        log.info("Fetching attendance records for user with id: {}", userId);

        if (!userRepository.existsById(userId)) {
            log.error("User not found with id: {}", userId);
            throw new NoSuchElementException("User not found with id: " + userId);
        }

        List<AttendanceResponse> attendances = attendanceRepository.findByUserId(userId).stream()
                .map(this::convertAttendanceToAttendanceResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching attendance records for user, total records found: {}", attendances.size());
        return attendances;
    }

    public List<AttendanceResponse> getAttendanceByProgrammeId(Long programmeId) {
        log.info("Fetching attendance records for programme with id: {}", programmeId);

        if (!programmeRepository.existsById(programmeId)) {
            log.error("Programme not found with id: {}", programmeId);
            throw new NoSuchElementException("Programme not found with id: " + programmeId);
        }

        List<AttendanceResponse> attendances = attendanceRepository.findByProgrammeId(programmeId).stream()
                .map(this::convertAttendanceToAttendanceResponse)
                .collect(Collectors.toList());
        log.info("Finished fetching attendance records for programme, total records found: {}", attendances.size());
        return attendances;
    }

    public AttendanceResponse updateAttendanceStatus(Long id, String status) {
        log.info("Updating attendance status for id: {} with status: {}", id, status);

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Attendance not found with id: " + id));

        attendance.setStatus(status);
        attendance = attendanceRepository.save(attendance);

        log.info("Attendance status updated successfully with id: {}", attendance.getId());
        return convertAttendanceToAttendanceResponse(attendance);
    }

    public void deleteAttendance(Long id) {
        log.info("Deleting attendance record with id: {}", id);
        if (!attendanceRepository.existsById(id)) {
            log.error("Failed to delete attendance: Attendance not found with id: {}", id);
            throw new NoSuchElementException("Attendance not found with id: " + id);
        }

        attendanceRepository.deleteById(id);
        log.info("Attendance deleted successfully with id: {}", id);
    }

    private AttendanceResponse convertAttendanceToAttendanceResponse(Attendance attendance) {
        if (attendance == null) {
            return null;
        }

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .userId(attendance.getUser().getId())
                .userName(attendance.getUser().getName())
                .programmeId(attendance.getProgramme().getId())
                .programmeName(attendance.getProgramme().getName())
                .time(attendance.getTime())
                .status(attendance.getStatus())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
