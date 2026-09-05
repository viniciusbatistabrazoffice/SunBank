package com.backend.dto;

import com.backend.entity.Operacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacaoResponse {

    private Long id;
    private String tipo;
    private BigDecimal valor;
    private String descricao;
    private String origem;
    private String destino;
    private LocalDateTime createdAt;

    public OperacaoResponse(Operacao operacao) {
        this.id = operacao.getId();
        this.tipo = operacao.getTipo().name();
        this.valor = operacao.getValor();
        this.descricao = operacao.getDescricao();
        this.origem = operacao.getOrigem().getUsername();
        this.destino = operacao.getDestino() != null ? operacao.getDestino().getUsername() : null;
        this.createdAt = operacao.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
