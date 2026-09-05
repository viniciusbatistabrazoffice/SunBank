package com.backend.repository;

import com.backend.entity.Auth;
import com.backend.entity.CriptoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriptoOperacaoRepository extends JpaRepository<CriptoOperacao, Long> {

    List<CriptoOperacao> findByOrigemOrDestinoOrderByCreatedAtDesc(Auth origem, Auth destino);
}
