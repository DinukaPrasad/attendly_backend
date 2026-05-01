package com.attendly.attendly_backend.modules.attendance.repo;

import java.util.List;

import com.attendly.attendly_backend.modules.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserId(Long userId);

    List<Attendance> findByProgrammeId(Long programmeId);
}
