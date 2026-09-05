package com.backend.service;

import com.backend.dto.OperacaoRequest;
import com.backend.dto.OperacaoResponse;
import com.backend.dto.SaldoResponse;

import java.util.List;

public interface OperacaoService {

    OperacaoResponse depositar(String token, OperacaoRequest request);

    OperacaoResponse sacar(String token, OperacaoRequest request);

    OperacaoResponse transferir(String token, OperacaoRequest request);

    SaldoResponse saldo(String token);

    List<OperacaoResponse> extrato(String token);
}
