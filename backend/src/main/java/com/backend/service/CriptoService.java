package com.backend.service;

import com.backend.dto.CriptoCarteiraResponse;
import com.backend.dto.CriptoOperacaoRequest;
import com.backend.dto.CriptoOperacaoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CriptoService {

    CriptoOperacaoResponse comprar(String token, CriptoOperacaoRequest request);

    CriptoOperacaoResponse vender(String token, CriptoOperacaoRequest request);

    CriptoOperacaoResponse transferir(String token, CriptoOperacaoRequest request);

    CriptoCarteiraResponse carteira(String token);

    List<CriptoOperacaoResponse> extrato(String token);

    Map<String, BigDecimal> cotacoes();
}
