package com.backend.controller;

import com.backend.entity.Operacao;
import com.backend.service.OperacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OperacaoController {

    private final OperacaoService operacaoService;

    public OperacaoController(OperacaoService operacaoService) {
        this.operacaoService = operacaoService;
    }

    @PostMapping("/save")
    public ResponseEntity<Operacao> save(Operacao operacao){
        try {
            Operacao operacao1 = operacaoService.save(operacao);

            ResponseEntity.ok(operacao1);
        }catch (Exception ex){
            ResponseEntity.internalServerError();
        }
    }

    @GetMapping("/list")

    @PutMapping("/edit")

    @DeleteMapping("/delete")
}
