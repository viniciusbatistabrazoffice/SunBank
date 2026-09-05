package com.backend.dto;

import java.time.LocalDateTime;

public class CarteiraResponse {

    private Long id;
    private String endereco;
    private String rede;
    private LocalDateTime createdAt;

    public CarteiraResponse(Long id, String endereco, String rede, LocalDateTime createdAt) {
        this.id = id;
        this.endereco = endereco;
        this.rede = rede;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getRede() {
        return rede;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
