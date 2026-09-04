package com.backend.dto.bb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferenciaRequest {

    @JsonProperty("numeroRequisicao")
    private String numeroRequisicao;

    @JsonProperty("agenciaDebito")
    private String agenciaDebito;

    @JsonProperty("contaCorrenteDebito")
    private String contaCorrenteDebito;

    @JsonProperty("digitoVerificadorContaCorrente")
    private String digitoVerificadorContaCorrente;

    @JsonProperty("contaPagamentoCredito")
    private String contaPagamentoCredito;

    @JsonProperty("agenciaCredito")
    private String agenciaCredito;

    @JsonProperty("contaCorrenteCredito")
    private String contaCorrenteCredito;

    @JsonProperty("digitoVerificadorContaCorrenteCredito")
    private String digitoVerificadorContaCorrenteCredito;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("tipoTransferencia")
    private String tipoTransferencia;

    @JsonProperty("descricao")
    private String descricao;

    public String getNumeroRequisicao() {
        return numeroRequisicao;
    }

    public void setNumeroRequisicao(String numeroRequisicao) {
        this.numeroRequisicao = numeroRequisicao;
    }

    public String getAgenciaDebito() {
        return agenciaDebito;
    }

    public void setAgenciaDebito(String agenciaDebito) {
        this.agenciaDebito = agenciaDebito;
    }

    public String getContaCorrenteDebito() {
        return contaCorrenteDebito;
    }

    public void setContaCorrenteDebito(String contaCorrenteDebito) {
        this.contaCorrenteDebito = contaCorrenteDebito;
    }

    public String getDigitoVerificadorContaCorrente() {
        return digitoVerificadorContaCorrente;
    }

    public void setDigitoVerificadorContaCorrente(String digitoVerificadorContaCorrente) {
        this.digitoVerificadorContaCorrente = digitoVerificadorContaCorrente;
    }

    public String getContaPagamentoCredito() {
        return contaPagamentoCredito;
    }

    public void setContaPagamentoCredito(String contaPagamentoCredito) {
        this.contaPagamentoCredito = contaPagamentoCredito;
    }

    public String getAgenciaCredito() {
        return agenciaCredito;
    }

    public void setAgenciaCredito(String agenciaCredito) {
        this.agenciaCredito = agenciaCredito;
    }

    public String getContaCorrenteCredito() {
        return contaCorrenteCredito;
    }

    public void setContaCorrenteCredito(String contaCorrenteCredito) {
        this.contaCorrenteCredito = contaCorrenteCredito;
    }

    public String getDigitoVerificadorContaCorrenteCredito() {
        return digitoVerificadorContaCorrenteCredito;
    }

    public void setDigitoVerificadorContaCorrenteCredito(String digitoVerificadorContaCorrenteCredito) {
        this.digitoVerificadorContaCorrenteCredito = digitoVerificadorContaCorrenteCredito;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipoTransferencia() {
        return tipoTransferencia;
    }

    public void setTipoTransferencia(String tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
