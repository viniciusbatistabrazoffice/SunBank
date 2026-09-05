package com.backend.dto;

import java.math.BigDecimal;

public class CarteiraEnvioResponse {

    private String txHash;
    private String origem;
    private String destino;
    private BigDecimal valorEth;
    private String status;

    public CarteiraEnvioResponse(String txHash, String origem, String destino,
                                 BigDecimal valorEth, String status) {
        this.txHash = txHash;
        this.origem = origem;
        this.destino = destino;
        this.valorEth = valorEth;
        this.status = status;
    }

    public String getTxHash() {
        return txHash;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public BigDecimal getValorEth() {
        return valorEth;
    }

    public String getStatus() {
        return status;
    }
}
