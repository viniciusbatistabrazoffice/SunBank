package com.backend.entity;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operacoes")
public class Operacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoOperacao tipo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "conta_origem_id", nullable = false)
    private Long contaOrigemId;

    @Column(name = "conta_destino_id")
    private Long contaDestinoId;

    @Column(name = "conta_destino")
    private String contaDestino;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 255)
    private String descricao;

    @Column(name = "identificador_bancario", length = 100)
    private String identificadorBancario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOperacao status;

    public Operacao() {
        this.dataHora = LocalDateTime.now();
        this.status = StatusOperacao.PENDENTE;
    }

    public Operacao(TipoOperacao tipo, BigDecimal valor, Long contaOrigemId, Long contaDestinoId, String descricao) {
        this();
        this.tipo = tipo;
        this.valor = valor;
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoOperacao tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(Long contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdentificadorBancario() {
        return identificadorBancario;
    }

    public void setIdentificadorBancario(String identificadorBancario) {
        this.identificadorBancario = identificadorBancario;
    }

    public StatusOperacao getStatus() {
        return status;
    }

    public void setStatus(StatusOperacao status) {
        this.status = status;
    }

    public enum TipoOperacao {
        DEPOSITO,
        TRANSFERENCIA,
        CONVERSAO
    }

    public enum StatusOperacao {
        PENDENTE,
        CONCLUIDA,
        CANCELADA
    }
}
