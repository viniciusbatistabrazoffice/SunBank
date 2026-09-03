package com.backend.repository;

import com.backend.entity.Auth;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface AuthRepository extends CrudRepository<Auth, Long> {
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findBySessionToken(BigInteger sessionToken);
}
