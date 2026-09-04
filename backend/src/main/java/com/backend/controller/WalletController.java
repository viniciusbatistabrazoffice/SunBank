package com.backend.controller;

import com.backend.entity.WalletBalance;
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

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
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
}
