package com.backend.controller;

import com.backend.entity.VirtualCard;
import com.backend.entity.WalletBalance;
import com.backend.service.VirtualCardService;
import com.backend.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final VirtualCardService virtualCardService;

    public WalletController(WalletService walletService, VirtualCardService virtualCardService) {
        this.walletService = walletService;
        this.virtualCardService = virtualCardService;
    }

    @GetMapping("/balances")
    public ResponseEntity<List<Map<String, Object>>> balances(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            List<WalletBalance> balances = walletService.getBalancesForClient(authHeader);
            List<Map<String, Object>> result = balances.stream()
                    .map(b -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("currency", b.getCurrency());
                        item.put("amount", b.getAmount());
                        return item;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Map<String, Object>>> cards(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            List<VirtualCard> cards = virtualCardService.getCardsForClient(authHeader);
            List<Map<String, Object>> result = cards.stream()
                    .map(c -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", c.getId());
                        item.put("type", c.getType());
                        item.put("label", c.getLabel());
                        item.put("brand", c.getBrand());
                        item.put("number", c.getNumber());
                        item.put("holder", c.getHolder());
                        item.put("expiry", c.getExpiry());
                        item.put("cvv", c.getCvv());
                        item.put("balance", c.getBalance());
                        item.put("limit", c.getLimit());
                        item.put("used", c.getUsed());
                        item.put("status", c.getStatus());
                        item.put("dueDate", c.getDueDate());
                        item.put("statement", c.getStatement());
                        item.put("lastDigits", c.getLastDigits());
                        item.put("active", c.isActive());
                        return item;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
