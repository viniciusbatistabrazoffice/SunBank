package com.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cripto_operacoes")
public class CriptoOperacao {

    public enum TipoCriptoOperacao {
        COMPRA,
        VENDA,
        TRANSFERENCIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCriptoOperacao tipo;

    @Column(nullable = false)
    private String simbolo;

    @Column(nullable = false, precision = 30, scale = 8)
    private BigDecimal quantidade;

    @Column(precision = 15, scale = 2)
    private BigDecimal precoUnitario;

    @Column(precision = 15, scale = 2)
    private BigDecimal valorTotal;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem_id", nullable = false)
    private Auth origem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id")
    private Auth destino;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCriptoOperacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCriptoOperacao tipo) {
        this.tipo = tipo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Auth getOrigem() {
        return origem;
    }

    public void setOrigem(Auth origem) {
        this.origem = origem;
    }

    public Auth getDestino() {
        return destino;
    }

    public void setDestino(Auth destino) {
        this.destino = destino;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
