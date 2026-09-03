package com.backend.service;

import com.backend.entity.Operacao;

import java.util.List;
import java.util.Optional;

public interface OperacaoService {
    Operacao save(Operacao operacao);
    Optional<List<Operacao>> list(Operacao operacao);
    Operacao update(Operacao operacao);
    Operacao delete(Operacao operacao);
}
