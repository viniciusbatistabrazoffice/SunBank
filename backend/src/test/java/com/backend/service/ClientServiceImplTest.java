package com.backend.service;

import com.backend.entity.Client;
import com.backend.entity.Cripto;
import com.backend.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CriptoService criptoService;

    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(clientRepository, criptoService);
    }

    @Test
    void saveShouldReturnSavedClient() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.save(client);

        assertEquals(client, result);
        assertNull(result.getCryptocurrencyTokenId());
        verify(clientRepository).save(client);
        verify(criptoService, never()).save(any());
    }

    @Test
    void saveShouldCreateCriptoWhenClientHasCriptoEscolhida() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setCriptoEscolhida("sunbraz");
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.save(client);

        ArgumentCaptor<Cripto> captor = ArgumentCaptor.forClass(Cripto.class);
        verify(criptoService).save(captor.capture());

        Cripto cripto = captor.getValue();
        assertEquals("SunBraz", cripto.getNome());
        assertEquals("SBZ", cripto.getSimbolo());
        assertEquals(client, cripto.getClient());
        assertNotNull(result.getCryptocurrencyTokenId());
        assertFalse(result.getCryptocurrencyTokenId().isBlank());
        assertNull(result.getCriptoEscolhida());
        verify(clientRepository).save(client);
    }

    @Test
    void saveShouldNotCreateCriptoWhenCriptoEscolhidaIsBlank() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setCriptoEscolhida("   ");
        when(clientRepository.save(client)).thenReturn(client);

        clientService.save(client);

        verify(criptoService, never()).save(any());
    }

    @Test
    void listShouldReturnAllClients() {
        Client client = new Client("Maria Souza", "09876543211", "maria@email.com", "11888888888");
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));

        List<Client> result = clientService.list();

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
