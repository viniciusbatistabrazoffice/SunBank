package com.backend.service;

import com.backend.entity.Client;
import com.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<List<Client>> list(Client client) {
        List<Client> clients = new ArrayList<>();
        clientRepository.findAll().forEach(clients::add);
        return Optional.of(clients);
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
