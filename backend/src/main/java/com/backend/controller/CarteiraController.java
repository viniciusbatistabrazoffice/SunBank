package com.backend.controller;

import com.backend.dto.CarteiraEnviarRequest;
import com.backend.dto.CarteiraEnvioResponse;
import com.backend.dto.CarteiraResponse;
import com.backend.dto.CarteiraSaldoResponse;
import com.backend.service.CarteiraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carteira")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @PostMapping
    public ResponseEntity<CarteiraResponse> criar(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carteiraService.criar(token));
    }

    @GetMapping
    public ResponseEntity<CarteiraResponse> consultar(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(carteiraService.consultar(token));
    }

    @GetMapping("/saldo")
    public ResponseEntity<CarteiraSaldoResponse> saldo(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(carteiraService.saldo(token));
    }

    @PostMapping("/enviar")
    public ResponseEntity<CarteiraEnvioResponse> enviar(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CarteiraEnviarRequest request) {
        return ResponseEntity.ok(carteiraService.enviar(token, request));
    }
}
