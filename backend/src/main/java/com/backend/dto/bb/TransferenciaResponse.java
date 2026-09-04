package com.backend.dto.bb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TransferenciaResponse {

    @JsonProperty("identificadorTransferencia")
    private String identificadorTransferencia;

    @JsonProperty("numeroRequisicao")
    private String numeroRequisicao;

    @JsonProperty("estadoRequisicao")
    private String estadoRequisicao;

    @JsonProperty("dataAgendamento")
    private LocalDateTime dataAgendamento;

    @JsonProperty("dataMovimento")
    private LocalDateTime dataMovimento;

    @JsonProperty("mensagem")
    private String mensagem;

    public String getIdentificadorTransferencia() {
        return identificadorTransferencia;
    }

    public void setIdentificadorTransferencia(String identificadorTransferencia) {
        this.identificadorTransferencia = identificadorTransferencia;
    }

    public String getNumeroRequisicao() {
        return numeroRequisicao;
    }

    public void setNumeroRequisicao(String numeroRequisicao) {
        this.numeroRequisicao = numeroRequisicao;
    }

    public String getEstadoRequisicao() {
        return estadoRequisicao;
    }

    public void setEstadoRequisicao(String estadoRequisicao) {
        this.estadoRequisicao = estadoRequisicao;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalDateTime getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDateTime dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
