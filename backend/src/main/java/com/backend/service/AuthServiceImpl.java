package com.backend.service;

import com.backend.entity.Auth;
import com.backend.repository.AuthRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final SecureRandom random = new SecureRandom();

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Auth signin(Auth auth) {
        Optional<Auth> existing = authRepository.findByUsername(auth.getUsername());
        if (existing.isEmpty() || !existing.get().getPassword().equals(auth.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        Auth user = existing.get();
        user.setToken(new BigInteger(64, random));
        return authRepository.save(user);
    }

    @Override
    public Auth signout(Auth auth) {
        Optional<Auth> existing = authRepository.findByToken(auth.getToken());
        if (existing.isEmpty()) {
            throw new RuntimeException("Session not found");
        }
        Auth user = existing.get();
        user.setToken(null);
        return authRepository.save(user);
    }

    @Override
    public Auth forgot(Auth auth) {
        Optional<Auth> existing = authRepository.findByUsername(auth.getUsername());
        if (existing.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Auth user = existing.get();
        user.setToken(new BigInteger(64, random));
        return authRepository.save(user);
    }

    @Override
    public Auth reset(Auth auth) {
        Optional<Auth> existing = authRepository.findByToken(auth.getToken());
        if (existing.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }
        Auth user = existing.get();
        if (auth.getPassword() != null && !auth.getPassword().isBlank()) {
            user.setPassword(auth.getPassword());
        }
        user.setToken(null);
        return authRepository.save(user);
    }
}
