package com.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CotacaoService {

    private static final Map<String, BigDecimal> COTACOES;

    static {
        Map<String, BigDecimal> cotacoes = new LinkedHashMap<>();
        cotacoes.put("BTC", new BigDecimal("350000.00"));
        cotacoes.put("ETH", new BigDecimal("18000.00"));
        cotacoes.put("SOL", new BigDecimal("800.00"));
        cotacoes.put("USDT", new BigDecimal("5.40"));
        COTACOES = Collections.unmodifiableMap(cotacoes);
    }

    public BigDecimal getPreco(String simbolo) {
        BigDecimal preco = COTACOES.get(normalizar(simbolo));
        if (preco == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criptomoeda não suportada: " + simbolo);
        }
        return preco;
    }

    public Map<String, BigDecimal> listarCotacoes() {
        return COTACOES;
    }

    public String normalizar(String simbolo) {
        if (simbolo == null || simbolo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o símbolo da criptomoeda");
        }
        return simbolo.trim().toUpperCase();
    }
}
