package com.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cripto_ativos", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "simbolo"}))
public class CriptoAtivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Auth usuario;

    @Column(nullable = false)
    private String simbolo;

    @Column(nullable = false, precision = 30, scale = 8)
    private BigDecimal quantidade = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auth getUsuario() {
        return usuario;
    }

    public void setUsuario(Auth usuario) {
        this.usuario = usuario;
    }

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
}
