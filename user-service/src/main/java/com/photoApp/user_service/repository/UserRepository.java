package com.photoApp.user_service.repository;

import com.photoApp.user_service.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByEmail(String email);
}
