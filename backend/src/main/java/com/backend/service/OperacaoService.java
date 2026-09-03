package com.backend.service;

import com.backend.entity.Operacao;

import java.util.List;
import java.util.Optional;

public interface OperacaoService {
    Operacao save(Operacao operacao);
    List<Operacao> list();
    Operacao update(Operacao operacao);
    Operacao delete(Operacao operacao);
}
