package com.backend.service;

import com.backend.entity.Cripto;

import java.util.List;

public interface CriptoService {
    Cripto save(Cripto cripto);
    List<Cripto> list();
    Cripto update(Cripto cripto);
    Cripto delete(Cripto cripto);
}
