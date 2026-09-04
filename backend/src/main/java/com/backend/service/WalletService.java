package com.backend.service;

import com.backend.entity.WalletBalance;

import java.util.List;

public interface WalletService {
    List<WalletBalance> getBalancesForClient(String authHeader);
}
