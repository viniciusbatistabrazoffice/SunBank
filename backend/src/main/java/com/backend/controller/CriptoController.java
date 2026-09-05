package com.backend.controller;

import com.backend.dto.CriptoCarteiraResponse;
import com.backend.dto.CriptoOperacaoRequest;
import com.backend.dto.CriptoOperacaoResponse;
import com.backend.service.CriptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cripto")
public class CriptoController {

    private final CriptoService criptoService;

    public CriptoController(CriptoService criptoService) {
        this.criptoService = criptoService;
    }

    @PostMapping("/comprar")
    public ResponseEntity<CriptoOperacaoResponse> comprar(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CriptoOperacaoRequest request) {
        return ResponseEntity.ok(criptoService.comprar(token, request));
    }

    @PostMapping("/vender")
    public ResponseEntity<CriptoOperacaoResponse> vender(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CriptoOperacaoRequest request) {
        return ResponseEntity.ok(criptoService.vender(token, request));
    }

    @PostMapping("/transferir")
    public ResponseEntity<CriptoOperacaoResponse> transferir(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CriptoOperacaoRequest request) {
        return ResponseEntity.ok(criptoService.transferir(token, request));
    }

    @GetMapping("/carteira")
    public ResponseEntity<CriptoCarteiraResponse> carteira(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(criptoService.carteira(token));
    }

    @GetMapping("/extrato")
    public ResponseEntity<List<CriptoOperacaoResponse>> extrato(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(criptoService.extrato(token));
    }

    @GetMapping("/cotacoes")
    public ResponseEntity<Map<String, BigDecimal>> cotacoes() {
        return ResponseEntity.ok(criptoService.cotacoes());
    }
}
