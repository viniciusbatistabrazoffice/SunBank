package com.backend.service;

import com.backend.entity.Auth;
import com.backend.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authRepository);
    }

    @Test
    void signinShouldReturnUserWithTokenWhenCredentialsAreValid() {
        Auth credentials = new Auth(null, "joao", "123456", null);
        Auth stored = new Auth(1L, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.signin(credentials);

        assertEquals("joao", result.getUsername());
        assertNotNull(result.getSessionToken());
        verify(authRepository).findByUsername("joao");
        verify(authRepository).save(stored);
    }

    @Test
    void signinShouldThrowRuntimeExceptionWhenUserNotFound() {
        Auth credentials = new Auth(null, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signin(credentials));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void signinShouldThrowRuntimeExceptionWhenPasswordDoesNotMatch() {
        Auth credentials = new Auth(null, "joao", "wrong", null);
        Auth stored = new Auth(1L, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signin(credentials));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void signoutShouldClearTokenWhenSessionExists() {
        BigInteger token = new BigInteger("123456");
        Auth session = new Auth(1L, "joao", "123456", token);
        when(authRepository.findBySessionToken(token)).thenReturn(Optional.of(session));
        when(authRepository.save(session)).thenReturn(session);

        Auth result = authService.signout(session);

        assertNull(result.getSessionToken());
        verify(authRepository).findBySessionToken(token);
        verify(authRepository).save(session);
    }

    @Test
    void signoutShouldThrowRuntimeExceptionWhenSessionNotFound() {
        BigInteger token = new BigInteger("123456");
        Auth session = new Auth(null, "joao", "123456", token);
        when(authRepository.findBySessionToken(token)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signout(session));

        assertEquals("Session not found", exception.getMessage());
        verify(authRepository).findBySessionToken(token);
        verify(authRepository, never()).save(any());
    }

    @Test
    void forgotShouldGenerateTokenWhenUserExists() {
        Auth request = new Auth(null, "joao", null, null);
        Auth stored = new Auth(1L, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.forgot(request);

        assertNotNull(result.getSessionToken());
        verify(authRepository).findByUsername("joao");
        verify(authRepository).save(stored);
    }

    @Test
    void forgotShouldThrowRuntimeExceptionWhenUserNotFound() {
        Auth request = new Auth(null, "joao", null, null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.forgot(request));

        assertEquals("User not found", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void resetShouldUpdatePasswordAndClearTokenWhenTokenIsValid() {
        BigInteger token = new BigInteger("123456");
        Auth stored = new Auth(1L, "joao", "123456", token);
        Auth request = new Auth(null, null, "newpass", token);
        when(authRepository.findBySessionToken(token)).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.reset(request);

        assertEquals("newpass", result.getPassword());
        assertNull(result.getSessionToken());
        verify(authRepository).findBySessionToken(token);
        verify(authRepository).save(stored);
    }

    @Test
    void resetShouldNotUpdatePasswordWhenPasswordIsBlank() {
        BigInteger token = new BigInteger("123456");
        Auth stored = new Auth(1L, "joao", "123456", token);
        Auth request = new Auth(null, null, "   ", token);
        when(authRepository.findBySessionToken(token)).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.reset(request);

        assertEquals("123456", result.getPassword());
        assertNull(result.getSessionToken());
        verify(authRepository).findBySessionToken(token);
        verify(authRepository).save(stored);
    }

    @Test
    void resetShouldThrowRuntimeExceptionWhenTokenIsInvalid() {
        BigInteger token = new BigInteger("123456");
        Auth request = new Auth(null, null, "newpass", token);
        when(authRepository.findBySessionToken(token)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.reset(request));

        assertEquals("Invalid reset token", exception.getMessage());
        verify(authRepository).findBySessionToken(token);
        verify(authRepository, never()).save(any());
    }
}
