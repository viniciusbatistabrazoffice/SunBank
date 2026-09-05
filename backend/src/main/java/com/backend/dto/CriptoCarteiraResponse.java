package com.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class CriptoCarteiraResponse {

    private String usuario;
    private List<CriptoPosicaoResponse> posicoes;
    private BigDecimal valorTotal;

    public CriptoCarteiraResponse(String usuario, List<CriptoPosicaoResponse> posicoes) {
        this.usuario = usuario;
        this.posicoes = posicoes;
        this.valorTotal = posicoes.stream()
                .map(CriptoPosicaoResponse::getValorAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getUsuario() {
        return usuario;
    }

    public List<CriptoPosicaoResponse> getPosicoes() {
        return posicoes;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
