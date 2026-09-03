package com.backend.controller;

import com.backend.entity.Client;
import com.backend.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        try {
            Client saved = clientService.save(client);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> list() {
        try {
            List<Client> clients = clientService.list(null).orElse(null);
            return ResponseEntity.ok(clients);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        try {
            client.setId(id);
            Client updated = clientService.update(client);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Client> delete(@PathVariable Long id) {
        try {
            Client client = new Client();
            client.setId(id);
            Client deleted = clientService.delete(client);
            return ResponseEntity.ok(deleted);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
