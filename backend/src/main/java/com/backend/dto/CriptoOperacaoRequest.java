package com.backend.dto;

import java.math.BigDecimal;

public class CriptoOperacaoRequest {

    private String simbolo;
    private BigDecimal quantidade;
    private String descricao;
    private String destinoUsername;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDestinoUsername() {
        return destinoUsername;
    }

    public void setDestinoUsername(String destinoUsername) {
        this.destinoUsername = destinoUsername;
    }
}
