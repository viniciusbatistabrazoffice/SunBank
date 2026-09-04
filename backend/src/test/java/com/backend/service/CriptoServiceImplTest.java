package com.backend.service;

import com.backend.entity.Client;
import com.backend.entity.Cripto;
import com.backend.repository.ClientRepository;
import com.backend.repository.CriptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriptoServiceImplTest {

    @Mock
    private CriptoRepository criptoRepository;

    @Mock
    private ClientRepository clientRepository;

    private CriptoServiceImpl criptoService;

    @BeforeEach
    void setUp() {
        criptoService = new CriptoServiceImpl(criptoRepository, clientRepository);
    }

    @Test
    void saveShouldForceSunBrazAndLinkToClient() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(1L);

        Cripto cripto = new Cripto();
        cripto.setClient(client);
        cripto.setNome("Bitcoin");
        cripto.setSimbolo("BTC");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(criptoRepository.save(any(Cripto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cripto result = criptoService.save(cripto);

        assertEquals("SunBraz", result.getNome());
        assertEquals("SBZ", result.getSimbolo());
        assertEquals(client, result.getClient());
        assertNotNull(result.getQuantidade());
        assertNotNull(result.getValor());
        assertNotNull(result.getDataHora());

        verify(clientRepository).findById(1L);
        verify(criptoRepository).save(result);
    }

    @Test
    void saveShouldThrowRuntimeExceptionWhenClientNotFound() {
        Client client = new Client("João Silva", "12345678900", "joao@email.com", "11999999999");
        client.setId(99L);

        Cripto cripto = new Cripto();
        cripto.setClient(client);

        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> criptoService.save(cripto));
        assertEquals("Client not found", exception.getMessage());

        verify(clientRepository).findById(99L);
        verify(criptoRepository, never()).save(any());
    }
}
