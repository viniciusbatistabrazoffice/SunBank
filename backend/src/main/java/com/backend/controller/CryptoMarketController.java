package com.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crypto")
public class CryptoMarketController {

    private static final Map<String, BigDecimal> PRICES = new LinkedHashMap<>();
    private static final List<Map<String, String>> CURRENCIES = List.of(
            Map.of("code", "BTC", "name", "Bitcoin"),
            Map.of("code", "ETH", "name", "Ethereum"),
            Map.of("code", "BRL", "name", "Real"),
            Map.of("code", "USDC", "name", "USD Coin"),
            Map.of("code", "SBZ", "name", "SunBraz")
    );

    static {
        PRICES.put("BTC", new BigDecimal("350000"));
        PRICES.put("ETH", new BigDecimal("18000"));
        PRICES.put("BRL", new BigDecimal("1"));
        PRICES.put("USDC", new BigDecimal("5.5"));
        PRICES.put("SBZ", new BigDecimal("1"));
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<String, BigDecimal>> prices() {
        return ResponseEntity.ok(PRICES);
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<Map<String, String>>> currencies() {
        return ResponseEntity.ok(CURRENCIES);
    }
}
