package com.backend.service;

import com.backend.entity.Operacao;
import com.backend.repository.OperacaoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperacaoServiceImpl implements OperacaoService {
    private final OperacaoRepository operacaoRepository;

    public OperacaoServiceImpl(OperacaoRepository operacaoRepository) {
        this.operacaoRepository = operacaoRepository;
    }

    @Override
    public Operacao save(Operacao operacao) {
        return operacaoRepository.save(operacao);
    }

    @Override
    public Optional<List<Operacao>> list(Operacao operacao) {
        List<Operacao> operacoes = new ArrayList<>();
        operacaoRepository.findAll().forEach(operacoes::add);
        return Optional.of(operacoes);
    }

    @Override
    public Operacao update(Operacao operacao) {
        Optional<Operacao> existing = operacaoRepository.findById(operacao.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Operação não encontrada");
        }
        return operacaoRepository.save(operacao);
    }

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
