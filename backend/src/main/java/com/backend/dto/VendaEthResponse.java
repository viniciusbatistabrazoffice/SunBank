package com.backend.dto;

import java.math.BigDecimal;

public class VendaEthResponse {

    private String txHash;
    private String enderecoExchange;
    private BigDecimal valorEth;
    private BigDecimal cotacaoBrl;
    private BigDecimal taxaPercent;
    private BigDecimal valorBrlLiquido;
    private String status;

    public VendaEthResponse(String txHash, String enderecoExchange, BigDecimal valorEth,
                            BigDecimal cotacaoBrl, BigDecimal taxaPercent,
                            BigDecimal valorBrlLiquido, String status) {
        this.txHash = txHash;
        this.enderecoExchange = enderecoExchange;
        this.valorEth = valorEth;
        this.cotacaoBrl = cotacaoBrl;
        this.taxaPercent = taxaPercent;
        this.valorBrlLiquido = valorBrlLiquido;
        this.status = status;
    }

    public String getTxHash() {
        return txHash;
    }

    public String getEnderecoExchange() {
        return enderecoExchange;
    }

    public BigDecimal getValorEth() {
        return valorEth;
    }

    public BigDecimal getCotacaoBrl() {
        return cotacaoBrl;
    }

    public BigDecimal getTaxaPercent() {
        return taxaPercent;
    }

    public BigDecimal getValorBrlLiquido() {
        return valorBrlLiquido;
    }

    public String getStatus() {
        return status;
    }
}
