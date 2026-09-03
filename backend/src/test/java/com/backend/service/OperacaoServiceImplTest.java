package com.backend.service;

import com.backend.entity.Operacao;
import com.backend.repository.OperacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacaoServiceImplTest {

    @Mock
    private OperacaoRepository operacaoRepository;

    private OperacaoServiceImpl operacaoService;

    @BeforeEach
    void setUp() {
        operacaoService = new OperacaoServiceImpl(operacaoRepository);
    }

    @Test
    void saveShouldReturnSavedOperacao() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.DEPOSITO,
                BigDecimal.valueOf(100.00),
                1L,
                null,
                "Depósito em conta"
        );
        when(operacaoRepository.save(operacao)).thenReturn(operacao);

        Operacao result = operacaoService.save(operacao);

        assertEquals(operacao, result);
        verify(operacaoRepository).save(operacao);
    }

    @Test
    void listShouldReturnAllOperacoes() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.SAQUE,
                BigDecimal.valueOf(50.00),
                2L,
                null,
                "Saque"
        );
        when(operacaoRepository.findAll()).thenReturn(Collections.singletonList(operacao));

        java.util.List<Operacao> result = operacaoService.list();

        assertEquals(1, result.size());
        assertEquals(operacao, result.get(0));
    }

    @Test
    void updateShouldReturnUpdatedOperacaoWhenOperacaoExists() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.TRANSFERENCIA,
                BigDecimal.valueOf(200.00),
                1L,
                2L,
                "Transferência"
        );
        operacao.setId(1L);
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        when(operacaoRepository.save(operacao)).thenReturn(operacao);

        Operacao result = operacaoService.update(operacao);

        assertEquals(operacao, result);
        verify(operacaoRepository).findById(1L);
        verify(operacaoRepository).save(operacao);
    }

    @Test
    void updateShouldThrowRuntimeExceptionWhenOperacaoNotFound() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.CONVERSAO,
                BigDecimal.valueOf(300.00),
                1L,
                null,
                "Conversão"
        );
        operacao.setId(1L);
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> operacaoService.update(operacao));

        assertEquals("Operação não encontrada", exception.getMessage());
        verify(operacaoRepository).findById(1L);
        verify(operacaoRepository, never()).save(any());
    }

    @Test
    void deleteShouldReturnDeletedOperacaoWhenOperacaoExists() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.DEPOSITO,
                BigDecimal.valueOf(100.00),
                1L,
                null,
                "Depósito"
        );
        operacao.setId(1L);
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        doNothing().when(operacaoRepository).delete(operacao);

        Operacao result = operacaoService.delete(operacao);

        assertEquals(operacao, result);
        verify(operacaoRepository).findById(1L);
        verify(operacaoRepository).delete(operacao);
    }

    @Test
    void deleteShouldThrowRuntimeExceptionWhenOperacaoNotFound() {
        Operacao operacao = new Operacao(
                Operacao.TipoOperacao.SAQUE,
                BigDecimal.valueOf(50.00),
                2L,
                null,
                "Saque"
        );
        operacao.setId(1L);
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> operacaoService.delete(operacao));

        assertEquals("Operação não encontrada", exception.getMessage());
        verify(operacaoRepository).findById(1L);
        verify(operacaoRepository, never()).delete(any());
    }
}
