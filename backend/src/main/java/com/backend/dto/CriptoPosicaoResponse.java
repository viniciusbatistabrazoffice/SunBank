package com.backend.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CriptoPosicaoResponse {

    private String simbolo;
    private BigDecimal quantidade;
    private BigDecimal cotacao;
    private BigDecimal valorAtual;

    public CriptoPosicaoResponse(String simbolo, BigDecimal quantidade, BigDecimal cotacao) {
        this.simbolo = simbolo;
        this.quantidade = quantidade;
        this.cotacao = cotacao;
        this.valorAtual = quantidade.multiply(cotacao).setScale(2, RoundingMode.HALF_UP);
    }

    public String getSimbolo() {
        return simbolo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public BigDecimal getCotacao() {
        return cotacao;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }
}
