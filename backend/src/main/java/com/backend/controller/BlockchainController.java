package com.backend.controller;

import com.backend.dto.BlockchainTransferRequest;
import com.backend.entity.Client;
import com.backend.repository.ClientRepository;
import com.backend.service.BlockchainAccountService;
import com.backend.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final BlockchainAccountService accountService;
    private final ClientRepository clientRepository;

    public BlockchainController(BlockchainService blockchainService,
                                BlockchainAccountService accountService,
                                ClientRepository clientRepository) {
        this.blockchainService = blockchainService;
        this.accountService = accountService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/wallet/{clientId}")
    public ResponseEntity<Map<String, Object>> createWallet(@PathVariable Long clientId) {
        try {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            if (client.getWalletAddress() != null) {
                Map<String, Object> existing = new HashMap<>();
                existing.put("address", client.getWalletAddress());
                existing.put("message", "Wallet already exists");
                return ResponseEntity.ok(existing);
            }

            BlockchainAccountService.GeneratedAccount account = accountService.generate();
            client.setWalletAddress(account.getAddress());
            client.setEncryptedPrivateKey(account.getEncryptedPrivateKey());
            clientRepository.save(client);

            Map<String, Object> result = new HashMap<>();
            result.put("address", account.getAddress());
            result.put("message", "Wallet created successfully");
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/balance/{clientId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long clientId) {
        try {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            if (client.getWalletAddress() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Client has no wallet"));
            }

            BigDecimal balance = blockchainService.getBalance(client.getWalletAddress());
            Map<String, Object> result = new HashMap<>();
            result.put("address", client.getWalletAddress());
            result.put("balance", balance);
            result.put("symbol", "SBZ");
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody BlockchainTransferRequest request) {
        try {
            if (request.getToAddress() == null || request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid toAddress or amount"));
            }

            String txHash;
            if (request.isFromTreasury()) {
                txHash = blockchainService.transferFromTreasury(request.getToAddress(), request.getAmount());
            } else {
                if (request.getFromClientId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "fromClientId is required"));
                }
                Client fromClient = clientRepository.findById(request.getFromClientId())
                        .orElseThrow(() -> new RuntimeException("Client not found"));
                if (fromClient.getEncryptedPrivateKey() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Client has no wallet"));
                }
                txHash = blockchainService.transferFromClient(
                        fromClient.getEncryptedPrivateKey(), request.getToAddress(), request.getAmount());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("txHash", txHash);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }
}
