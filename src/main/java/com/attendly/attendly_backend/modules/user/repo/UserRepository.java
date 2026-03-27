package com.attendly.attendly_backend.modules.user.repo;

import org.springframework.stereotype.Repository;

import com.attendly.attendly_backend.modules.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
