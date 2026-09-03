package com.backend.repository;

import com.backend.entity.Operacao;
import org.springframework.data.repository.CrudRepository;

public interface OperacaoRepository extends CrudRepository<Operacao, Long> {
}
