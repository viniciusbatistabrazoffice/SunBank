package com.backend.controller;

import com.backend.entity.Operacao;
import com.backend.service.OperacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operacoes")
public class OperacaoController {

    private final OperacaoService operacaoService;

    public OperacaoController(OperacaoService operacaoService) {
        this.operacaoService = operacaoService;
    }

    @PostMapping
    public ResponseEntity<Operacao> save(@RequestBody Operacao operacao) {
        try {
            Operacao saved = operacaoService.save(operacao);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Operacao>> list() {
        try {
            List<Operacao> operacoes = operacaoService.list();
            return ResponseEntity.ok(operacoes);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operacao> update(@PathVariable Long id, @RequestBody Operacao operacao) {
        try {
            operacao.setId(id);
            Operacao updated = operacaoService.update(operacao);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Operacao> delete(@PathVariable Long id) {
        try {
            Operacao operacao = new Operacao();
            operacao.setId(id);
            Operacao deleted = operacaoService.delete(operacao);
            return ResponseEntity.ok(deleted);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
