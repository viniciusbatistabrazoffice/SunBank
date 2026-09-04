package com.backend.service;

import com.backend.entity.Client;
import com.backend.entity.Cripto;
import com.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CriptoService criptoService;

    public ClientServiceImpl(ClientRepository clientRepository, CriptoService criptoService) {
        this.clientRepository = clientRepository;
        this.criptoService = criptoService;
    }

    @Transactional
    @Override
    public Client save(Client client) {
        String criptoEscolhida = client.getCriptoEscolhida();
        client.setCriptoEscolhida(null);

        if (criptoEscolhida != null && !criptoEscolhida.isBlank()) {
            client.setCryptocurrencyTokenId(UUID.randomUUID().toString().replace("-", ""));
        }

        Client savedClient = clientRepository.save(client);

        if (criptoEscolhida != null && !criptoEscolhida.isBlank()) {
            Cripto cripto = new Cripto();
            cripto.setClient(savedClient);
            cripto.setNome("SunBraz");
            cripto.setSimbolo("SBZ");
            criptoService.save(cripto);
        }

        return client;
    }

    @Override
    public List<Client> list() {
        List<Client> clients = new ArrayList<>();
        clientRepository.findAll().forEach(clients::add);
        return clients;
    }

    @Override
    public Client update(Client client) {
        Optional<Client> existing = clientRepository.findById(client.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        return clientRepository.save(client);
    }

    @Override
    public Client delete(Client client) {
        Optional<Client> existing = clientRepository.findById(client.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.delete(existing.get());
        return existing.get();
    }
}
