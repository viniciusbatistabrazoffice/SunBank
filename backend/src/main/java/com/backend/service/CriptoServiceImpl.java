package com.backend.service;

import com.backend.entity.Client;
import com.backend.entity.Cripto;
import com.backend.repository.ClientRepository;
import com.backend.repository.CriptoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CriptoServiceImpl implements CriptoService {

    private static final String NOME_PADRAO = "SunBraz";
    private static final String SIMBOLO_PADRAO = "SBZ";

    private final CriptoRepository criptoRepository;
    private final ClientRepository clientRepository;
    private final SecureRandom random;

    public CriptoServiceImpl(CriptoRepository criptoRepository, ClientRepository clientRepository) {
        this.criptoRepository = criptoRepository;
        this.clientRepository = clientRepository;
        this.random = new SecureRandom();
    }

    @Override
    public Cripto save(Cripto cripto) {
        Client client = clientRepository.findById(cripto.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        cripto.setClient(client);

        cripto.setNome(NOME_PADRAO);
        cripto.setSimbolo(SIMBOLO_PADRAO);

        if (cripto.getQuantidade() == null) {
            BigDecimal quantidade = BigDecimal.valueOf(random.nextDouble() * 100)
                    .setScale(8, RoundingMode.HALF_UP);
            cripto.setQuantidade(quantidade);
        }

        if (cripto.getValor() == null) {
            BigDecimal valor = BigDecimal.valueOf(1.0 + random.nextDouble() * 99999.0)
                    .setScale(2, RoundingMode.HALF_UP);
            cripto.setValor(valor);
        }

        return criptoRepository.save(cripto);
    }

    @Override
    public List<Cripto> list() {
        List<Cripto> criptos = new ArrayList<>();
        criptoRepository.findAll().forEach(criptos::add);
        return criptos;
    }

    @Override
    public Cripto update(Cripto cripto) {
        Optional<Cripto> existing = criptoRepository.findById(cripto.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Cripto not found");
        }
        Client client = clientRepository.findById(cripto.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        cripto.setClient(client);
        cripto.setNome(NOME_PADRAO);
        cripto.setSimbolo(SIMBOLO_PADRAO);
        return criptoRepository.save(cripto);
    }

    @Override
    public Cripto delete(Cripto cripto) {
        Optional<Cripto> existing = criptoRepository.findById(cripto.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Cripto not found");
        }
        criptoRepository.delete(existing.get());
        return existing.get();
    }
}
