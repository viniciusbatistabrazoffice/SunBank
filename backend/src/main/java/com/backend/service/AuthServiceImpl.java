package com.backend.service;

import com.backend.entity.Auth;
import com.backend.entity.Client;
import com.backend.repository.AuthRepository;
import com.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final ClientRepository clientRepository;
    private final SecureRandom random = new SecureRandom();

    public AuthServiceImpl(AuthRepository authRepository, ClientRepository clientRepository) {
        this.authRepository = authRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Auth signin(Auth auth) {
        Optional<Auth> existing = authRepository.findByUsername(auth.getUsername());
        if (existing.isEmpty() || !existing.get().getPassword().equals(auth.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        Auth user = existing.get();
        Client client = clientRepository.findById(user.getClientId()).orElse(null);
        if (client != null) {
            user.setCripto(client.getCryptocurrencyTokenId());
        }
        user.setAuthToken(new BigInteger(64, random));
        return authRepository.save(user);
    }

    @Override
    public Auth signout(Auth auth) {
        Optional<Auth> existing = authRepository.findByAuthToken(auth.getAuthToken());
        if (existing.isEmpty()) {
            throw new RuntimeException("Session not found");
        }
        Auth user = existing.get();
        user.setAuthToken(null);
        return authRepository.save(user);
    }

    @Override
    public Auth forgot(Auth auth) {
        Optional<Auth> existing = authRepository.findByUsername(auth.getUsername());
        if (existing.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Auth user = existing.get();
        user.setAuthToken(new BigInteger(64, random));
        return authRepository.save(user);
    }

    @Override
    public Auth reset(Auth auth) {
        Optional<Auth> existing = authRepository.findByAuthToken(auth.getAuthToken());
        if (existing.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }
        Auth user = existing.get();
        if (auth.getPassword() != null && !auth.getPassword().isBlank()) {
            user.setPassword(auth.getPassword());
        }
        user.setAuthToken(null);
        return authRepository.save(user);
    }

    @Override
    public Auth signup(Auth auth) {
        if (auth.getUsername() == null || auth.getUsername().isBlank()
                || auth.getPassword() == null || auth.getPassword().isBlank()
                || auth.getEmail() == null || auth.getEmail().isBlank()
                || auth.getClientId() == null) {
            throw new RuntimeException("Missing required fields");
        }
        if (authRepository.findByUsername(auth.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Auth user = new Auth();
        user.setUsername(auth.getUsername());
        user.setPassword(auth.getPassword());
        user.setEmail(auth.getEmail());
        user.setClientId(auth.getClientId());
        return authRepository.save(user);
    }
}
