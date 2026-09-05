package com.backend.dto;

import java.math.BigDecimal;

public class CarteiraEnviarRequest {

    private String destino;
    private BigDecimal valorEth;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public BigDecimal getValorEth() {
        return valorEth;
    }

    public void setValorEth(BigDecimal valorEth) {
        this.valorEth = valorEth;
    }
}
