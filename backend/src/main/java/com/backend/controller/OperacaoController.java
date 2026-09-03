package com.backend.controller;

import com.backend.service.OperacaoService;
import org.springframework.web.bind.annotation.*;

@RestController
public class OperacaoController {

    private final OperacaoService operacaoService;

    public OperacaoController(OperacaoService operacaoService) {
        this.operacaoService = operacaoService;
    }

    @PostMapping("/save")

    @GetMapping("/list")

    @PutMapping("/edit")

    @DeleteMapping("/delete")
}
