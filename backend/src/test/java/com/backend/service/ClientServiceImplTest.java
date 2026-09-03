package com.backend.service;

import com.backend.entity.Client;
import com.backend.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    void saveShouldReturnSavedClient() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.save(client);

        assertEquals(client, result);
        verify(clientRepository).save(client);
    }

    @Test
    void listShouldReturnAllClients() {
        Client client = new Client("Maria Souza", "09876543211", "maria@email.com", "11888888888");
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));

        java.util.List<Client> result = clientService.list();

        assertEquals(1, result.size());
        assertEquals(client, result.get(0));
    }

    @Test
    void updateShouldReturnUpdatedClientWhenClientExists() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.update(client);

        assertEquals(client, result);
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(client);
    }

    @Test
    void updateShouldThrowRuntimeExceptionWhenClientNotFound() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.update(client));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteShouldReturnDeletedClientWhenClientExists() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        Client result = clientService.delete(client);

        assertEquals(client, result);
        verify(clientRepository).findById(1L);
        verify(clientRepository).delete(client);
    }

    @Test
    void deleteShouldThrowRuntimeExceptionWhenClientNotFound() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.delete(client));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).delete(any());
    }
}
