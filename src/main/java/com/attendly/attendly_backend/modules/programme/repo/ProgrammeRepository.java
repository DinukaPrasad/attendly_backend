package com.attendly.attendly_backend.modules.programme.repo;

import com.attendly.attendly_backend.modules.programme.model.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

}
