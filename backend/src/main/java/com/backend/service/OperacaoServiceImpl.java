package com.backend.service;

import com.backend.entity.Operacao;
import com.backend.repository.OperacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperacaoServiceImpl implements OperacaoService {
    private final OperacaoRepository operacaoRepository;
    private final BancoDoBrasilService bancoDoBrasilService;

    public OperacaoServiceImpl(OperacaoRepository operacaoRepository, BancoDoBrasilService bancoDoBrasilService) {
        this.operacaoRepository = operacaoRepository;
        this.bancoDoBrasilService = bancoDoBrasilService;
    }

    @Transactional
    @Override
    public Operacao save(Operacao operacao) {
        Operacao saved = operacaoRepository.save(operacao);

        if (saved.getTipo() == Operacao.TipoOperacao.TRANSFERENCIA
                && saved.getContaDestino() != null
                && !saved.getContaDestino().isBlank()) {
            String identificador = bancoDoBrasilService.transferir(saved.getValor(), saved.getContaDestino());
            saved.setIdentificadorBancario(identificador);
            saved.setStatus(Operacao.StatusOperacao.CONCLUIDA);
            saved = operacaoRepository.save(saved);
        }

        return saved;
    }

    @Override
    public List<Operacao> list() {
        List<Operacao> operacoes = new ArrayList<>();
        operacaoRepository.findAll().forEach(operacoes::add);
        return operacoes;
    }

    @Transactional
    @Override
    public Operacao update(Operacao operacao) {
        Optional<Operacao> existing = operacaoRepository.findById(operacao.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Operação não encontrada");
        }
        return operacaoRepository.save(operacao);
    }

    @Transactional
    @Override
    public Operacao delete(Operacao operacao) {
        Optional<Operacao> existing = operacaoRepository.findById(operacao.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Operação não encontrada");
        }
        operacaoRepository.delete(existing.get());
        return existing.get();
    }
}
