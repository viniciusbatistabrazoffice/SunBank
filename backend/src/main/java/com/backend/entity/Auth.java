package com.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "auth")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "auth_token")
    private BigInteger authToken;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    public Auth() {
    }

    public Auth(Long id, String username, String password, String email, Long clientId, BigInteger authToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.clientId = clientId;
        this.authToken = authToken;
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        this.dataAtualizacao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigInteger getAuthToken() {
        return authToken;
    }

    public void setAuthToken(BigInteger authToken) {
        this.authToken = authToken;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
