package com.backend.dto;

import java.math.BigDecimal;

public class OperacaoRequest {

    private BigDecimal valor;
    private String descricao;
    private String destinoUsername;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
