package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carteiras")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Auth usuario;

    @Column(unique = true, nullable = false, length = 42)
    private String endereco;

    @Column(nullable = false)
    private String chavePrivadaCriptografada;

    private LocalDateTime createdAt = LocalDateTime.now();

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getChavePrivadaCriptografada() {
        return chavePrivadaCriptografada;
    }

    public void setChavePrivadaCriptografada(String chavePrivadaCriptografada) {
        this.chavePrivadaCriptografada = chavePrivadaCriptografada;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
