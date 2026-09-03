package com.backend.service;

import com.backend.entity.Operacao;
import com.backend.repository.OperacaoRepository;

import java.util.List;
import java.util.Optional;

public class OperacaoServiceImpl implements OperacaoService{
    private final OperacaoRepository operacaoRepository;

    public OperacaoServiceImpl(OperacaoRepository operacaoRepository) {
        this.operacaoRepository = operacaoRepository;
    }

    @Override
    public Operacao save(Operacao operacao) {
        return null;
    }

    @Override
    public Optional<List<Operacao>> list(Operacao operacao) {
        return Optional.empty();
    }

    @Override
    public Operacao update(Operacao operacao) {
        return null;
    }

    @Override
    public Operacao delete(Operacao operacao) {
        return null;
    }
}
