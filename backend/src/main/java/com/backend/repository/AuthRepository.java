package com.backend.repository;

import com.backend.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findByEmail(String email);
    Optional<Auth> findByToken(String token);
}
