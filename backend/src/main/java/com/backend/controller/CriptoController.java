package com.backend.controller;

import com.backend.entity.Cripto;
import com.backend.service.CriptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/criptos")
public class CriptoController {

    private final CriptoService criptoService;

    public CriptoController(CriptoService criptoService) {
        this.criptoService = criptoService;
    }

    @PostMapping
    public ResponseEntity<Cripto> save(@RequestBody Cripto cripto) {
        try {
            Cripto saved = criptoService.save(cripto);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Cripto>> list() {
        try {
            List<Cripto> criptos = criptoService.list();
            return ResponseEntity.ok(criptos);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cripto> update(@PathVariable Long id, @RequestBody Cripto cripto) {
        try {
            cripto.setId(id);
            Cripto updated = criptoService.update(cripto);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cripto> delete(@PathVariable Long id) {
        try {
            Cripto cripto = new Cripto();
            cripto.setId(id);
            Cripto deleted = criptoService.delete(cripto);
            return ResponseEntity.ok(deleted);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
