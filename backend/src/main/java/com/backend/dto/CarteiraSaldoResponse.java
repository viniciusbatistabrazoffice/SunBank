package com.backend.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CarteiraSaldoResponse {

    private String endereco;
    private BigInteger saldoWei;
    private BigDecimal saldoEth;

    public CarteiraSaldoResponse(String endereco, BigInteger saldoWei, BigDecimal saldoEth) {
        this.endereco = endereco;
        this.saldoWei = saldoWei;
        this.saldoEth = saldoEth;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigInteger getSaldoWei() {
        return saldoWei;
    }

    public BigDecimal getSaldoEth() {
        return saldoEth;
    }
}
