package com.backend.dto;

import com.backend.entity.CriptoOperacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CriptoOperacaoResponse {

    private Long id;
    private String tipo;
    private String simbolo;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private String descricao;
    private String origem;
    private String destino;
    private LocalDateTime createdAt;

    public CriptoOperacaoResponse(CriptoOperacao operacao) {
        this.id = operacao.getId();
        this.tipo = operacao.getTipo().name();
        this.simbolo = operacao.getSimbolo();
        this.quantidade = operacao.getQuantidade();
        this.precoUnitario = operacao.getPrecoUnitario();
        this.valorTotal = operacao.getValorTotal();
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

    public String getSimbolo() {
        return simbolo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
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
