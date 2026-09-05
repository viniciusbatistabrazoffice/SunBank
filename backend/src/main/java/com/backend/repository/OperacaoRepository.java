package com.backend.repository;

import com.backend.entity.Auth;
import com.backend.entity.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

    List<Operacao> findByOrigemOrDestinoOrderByCreatedAtDesc(Auth origem, Auth destino);

    @Query("SELECT COALESCE(SUM(o.valor), 0) FROM Operacao o " +
            "WHERE o.destino = :usuario OR (o.origem = :usuario AND o.tipo = 'DEPOSITO')")
    BigDecimal somaCreditos(@Param("usuario") Auth usuario);

    @Query("SELECT COALESCE(SUM(o.valor), 0) FROM Operacao o " +
            "WHERE o.origem = :usuario AND o.tipo IN ('SAQUE', 'TRANSFERENCIA')")
    BigDecimal somaDebitos(@Param("usuario") Auth usuario);
}
