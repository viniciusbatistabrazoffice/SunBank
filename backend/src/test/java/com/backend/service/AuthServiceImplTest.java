package com.backend.service;

import com.backend.entity.Auth;
import com.backend.entity.Client;
import com.backend.repository.AuthRepository;
import com.backend.repository.ClientRepository;
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

    @Mock
    private ClientRepository clientRepository;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authRepository, clientRepository);
    }

    private Auth auth(Long id, String username, String password, BigInteger token) {
        return new Auth(id, username, password, "user@example.com", 1L, token);
    }

    @Test
    void signinShouldReturnUserWithTokenWhenCredentialsAreValid() {
        Auth credentials = auth(null, "joao", "123456", null);
        Auth stored = auth(1L, "joao", "123456", null);
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setCryptocurrencyTokenId("token123");
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.signin(credentials);

        assertEquals("joao", result.getUsername());
        assertNotNull(result.getAuthToken());
        assertEquals("token123", result.getCripto());
        verify(authRepository).findByUsername("joao");
        verify(authRepository).save(stored);
    }

    @Test
    void signinShouldThrowRuntimeExceptionWhenUserNotFound() {
        Auth credentials = auth(null, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signin(credentials));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void signinShouldThrowRuntimeExceptionWhenPasswordDoesNotMatch() {
        Auth credentials = auth(null, "joao", "wrong", null);
        Auth stored = auth(1L, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signin(credentials));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void signoutShouldClearTokenWhenSessionExists() {
        BigInteger token = new BigInteger("123456");
        Auth session = auth(1L, "joao", "123456", token);
        when(authRepository.findByAuthToken(token)).thenReturn(Optional.of(session));
        when(authRepository.save(session)).thenReturn(session);

        Auth result = authService.signout(session);

        assertNull(result.getAuthToken());
        verify(authRepository).findByAuthToken(token);
        verify(authRepository).save(session);
    }

    @Test
    void signoutShouldThrowRuntimeExceptionWhenSessionNotFound() {
        BigInteger token = new BigInteger("123456");
        Auth session = auth(null, "joao", "123456", token);
        when(authRepository.findByAuthToken(token)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signout(session));

        assertEquals("Session not found", exception.getMessage());
        verify(authRepository).findByAuthToken(token);
        verify(authRepository, never()).save(any());
    }

    @Test
    void forgotShouldGenerateTokenWhenUserExists() {
        Auth request = auth(null, "joao", null, null);
        Auth stored = auth(1L, "joao", "123456", null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.forgot(request);

        assertNotNull(result.getAuthToken());
        verify(authRepository).findByUsername("joao");
        verify(authRepository).save(stored);
    }

    @Test
    void forgotShouldThrowRuntimeExceptionWhenUserNotFound() {
        Auth request = auth(null, "joao", null, null);
        when(authRepository.findByUsername("joao")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.forgot(request));

        assertEquals("User not found", exception.getMessage());
        verify(authRepository).findByUsername("joao");
        verify(authRepository, never()).save(any());
    }

    @Test
    void resetShouldUpdatePasswordAndClearTokenWhenTokenIsValid() {
        BigInteger token = new BigInteger("123456");
        Auth stored = auth(1L, "joao", "123456", token);
        Auth request = auth(null, null, "newpass", token);
        when(authRepository.findByAuthToken(token)).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.reset(request);

        assertEquals("newpass", result.getPassword());
        assertNull(result.getAuthToken());
        verify(authRepository).findByAuthToken(token);
        verify(authRepository).save(stored);
    }

    @Test
    void resetShouldNotUpdatePasswordWhenPasswordIsBlank() {
        BigInteger token = new BigInteger("123456");
        Auth stored = auth(1L, "joao", "123456", token);
        Auth request = auth(null, null, "   ", token);
        when(authRepository.findByAuthToken(token)).thenReturn(Optional.of(stored));
        when(authRepository.save(stored)).thenReturn(stored);

        Auth result = authService.reset(request);

        assertEquals("123456", result.getPassword());
        assertNull(result.getAuthToken());
        verify(authRepository).findByAuthToken(token);
        verify(authRepository).save(stored);
    }

    @Test
    void resetShouldThrowRuntimeExceptionWhenTokenIsInvalid() {
        BigInteger token = new BigInteger("123456");
        Auth request = auth(null, null, "newpass", token);
        when(authRepository.findByAuthToken(token)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.reset(request));

        assertEquals("Invalid reset token", exception.getMessage());
        verify(authRepository).findByAuthToken(token);
        verify(authRepository, never()).save(any());
    }
}
