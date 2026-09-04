package com.backend.controller;

import com.backend.entity.Cripto;
import com.backend.service.CriptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriptoControllerTest {

    @Mock
    private CriptoService criptoService;

    private CriptoController criptoController;

    @BeforeEach
    void setUp() {
        criptoController = new CriptoController(criptoService);
    }

    @Test
    void saveShouldReturnSavedCripto() {
        Cripto cripto = new Cripto();
        when(criptoService.save(cripto)).thenReturn(cripto);

        ResponseEntity<Cripto> result = criptoController.save(cripto);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(cripto, result.getBody());
        verify(criptoService).save(cripto);
    }

    @Test
    void listShouldReturnAllCriptos() {
        Cripto cripto = new Cripto();
        when(criptoService.list()).thenReturn(Collections.singletonList(cripto));

        ResponseEntity<List<Cripto>> result = criptoController.list();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        verify(criptoService).list();
    }

    @Test
    void saveShouldReturn500WhenServiceThrows() {
        Cripto cripto = new Cripto();
        when(criptoService.save(cripto)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Cripto> result = criptoController.save(cripto);

        assertEquals(500, result.getStatusCodeValue());
        verify(criptoService).save(cripto);
    }
}
