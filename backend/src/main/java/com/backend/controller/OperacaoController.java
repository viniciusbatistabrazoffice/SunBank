package com.backend.controller;

import com.backend.service.OperacaoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperacaoController {

    private final OperacaoService operacaoService;

    public OperacaoController(OperacaoService operacaoService) {
        this.operacaoService = operacaoService;
    }
}
