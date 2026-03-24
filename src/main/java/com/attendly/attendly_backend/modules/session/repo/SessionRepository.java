package com.attendly.attendly_backend.modules.session.repo;

import com.attendly.attendly_backend.modules.session.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

}
