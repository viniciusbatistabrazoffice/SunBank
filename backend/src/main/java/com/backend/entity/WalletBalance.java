package com.backend.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_balances", indexes = {
        @Index(name = "idx_wallet_client_currency", columnList = "client_id, currency", unique = true)
})
public class WalletBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, length = 20)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;

    public WalletBalance() {
    }

    public WalletBalance(Client client, String currency, BigDecimal amount) {
        this.client = client;
        this.currency = currency;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
