package com.attendly.attendly_backend.modules.attendance.controller;

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

import com.attendly.attendly_backend.modules.attendance.dto.AttendanceResponse;
import com.attendly.attendly_backend.modules.attendance.dto.CreateAttendanceRequest;
import com.attendly.attendly_backend.modules.attendance.service.AttendanceService;
import com.attendly.attendly_backend.utility.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> createAttendance(
            @Valid @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse attendance = attendanceService.createAttendance(request);
        return ResponseEntity.ok(ApiResponse.success("Attendance marked successfully", attendance));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance() {
        List<AttendanceResponse> attendances = attendanceService.getAllAttendance();
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved successfully", attendances));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendanceById(@PathVariable Long id) {
        AttendanceResponse attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance record retrieved successfully", attendance));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByUserId(
            @PathVariable Long userId) {
        List<AttendanceResponse> attendances = attendanceService.getAttendanceByUserId(userId);
        return ResponseEntity
                .ok(ApiResponse.success("Attendance records for user retrieved successfully", attendances));
    }

    @GetMapping("/programme/{programmeId}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByProgrammeId(
            @PathVariable Long programmeId) {
        List<AttendanceResponse> attendances = attendanceService.getAttendanceByProgrammeId(programmeId);
        return ResponseEntity
                .ok(ApiResponse.success("Attendance records for programme retrieved successfully", attendances));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LECTURER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendanceStatus(@PathVariable Long id,
            @Valid @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse attendance = attendanceService.updateAttendanceStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Attendance status updated successfully", attendance));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance deleted successfully"));
    }
}
