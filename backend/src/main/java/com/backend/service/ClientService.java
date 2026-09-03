package com.backend.service;

import com.backend.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client save(Client client);
    Optional<List<Client>> list(Client client);
    Client update(Client client);
    Client delete(Client client);
}
