package com.backend.service;

import com.backend.dto.AuthResponse;
import com.backend.dto.LoginRequest;
import com.backend.dto.RegisterRequest;
import com.backend.entity.Auth;
import com.backend.repository.AuthRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preencha todos os campos obrigatórios");
        }

        if (authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário já existe");
        }

        if (authRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }

        String salt = generateSalt();
        Auth auth = new Auth();
        auth.setUsername(request.getUsername().trim());
        auth.setEmail(request.getEmail().trim());
        auth.setSalt(salt);
        auth.setPasswordHash(hashPassword(request.getPassword(), salt));
        auth.setToken(generateToken());

        Auth saved = authRepository.save(auth);
        return new AuthResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getToken());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preencha todos os campos obrigatórios");
        }

        Auth auth = authRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!hashPassword(request.getPassword(), auth.getSalt()).equals(auth.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = generateToken();
        auth.setToken(token);
        Auth saved = authRepository.save(auth);
        return new AuthResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getToken());
    }

    @Override
    public AuthResponse me(String token) {
        String clean = extractToken(token);
        Auth auth = authRepository.findByToken(clean)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
        return new AuthResponse(auth.getId(), auth.getUsername(), auth.getEmail(), auth.getToken());
    }

    @Override
    public void logout(String token) {
        String clean = extractToken(token);
        Auth auth = authRepository.findByToken(clean)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
        auth.setToken(null);
        authRepository.save(auth);
    }

    private String hashPassword(String password, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt.getBytes(StandardCharsets.UTF_8),
                    10000,
                    256
            );
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a senha", e);
        }
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generateToken() {
        byte[] token = new byte[32];
        new SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    private String extractToken(String header) {
        if (header == null || header.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token não informado");
        }
        if (header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return header;
    }
}
