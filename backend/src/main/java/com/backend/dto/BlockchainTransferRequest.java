package com.backend.dto;

import java.math.BigDecimal;

public class BlockchainTransferRequest {

    private Long fromClientId;
    private String toAddress;
    private BigDecimal amount;
    private boolean fromTreasury;

    public Long getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(Long fromClientId) {
        this.fromClientId = fromClientId;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isFromTreasury() {
        return fromTreasury;
    }

    public void setFromTreasury(boolean fromTreasury) {
        this.fromTreasury = fromTreasury;
    }
}
