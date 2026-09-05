package com.backend.controller;

import com.backend.dto.OperacaoRequest;
import com.backend.dto.OperacaoResponse;
import com.backend.dto.SaldoResponse;
import com.backend.service.OperacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operacoes")
public class OperacoesController {

    private final OperacaoService operacaoService;

    public OperacoesController(OperacaoService operacaoService) {
        this.operacaoService = operacaoService;
    }

    @PostMapping("/deposito")
    public ResponseEntity<OperacaoResponse> depositar(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody OperacaoRequest request) {
        return ResponseEntity.ok(operacaoService.depositar(token, request));
    }

    @PostMapping("/saque")
    public ResponseEntity<OperacaoResponse> sacar(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody OperacaoRequest request) {
        return ResponseEntity.ok(operacaoService.sacar(token, request));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<OperacaoResponse> transferir(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody OperacaoRequest request) {
        return ResponseEntity.ok(operacaoService.transferir(token, request));
    }

    @GetMapping("/saldo")
    public ResponseEntity<SaldoResponse> saldo(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(operacaoService.saldo(token));
    }

    @GetMapping("/extrato")
    public ResponseEntity<List<OperacaoResponse>> extrato(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(operacaoService.extrato(token));
    }
}
