package com.backend.service;

import com.backend.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client save(Client client);
    List<Client> list();
    Client update(Client client);
    Client delete(Client client);
}
