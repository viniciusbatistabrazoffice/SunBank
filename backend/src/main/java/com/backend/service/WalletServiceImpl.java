package com.backend.service;

import com.backend.entity.Auth;
import com.backend.entity.WalletBalance;
import com.backend.repository.AuthRepository;
import com.backend.repository.WalletBalanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletBalanceRepository walletBalanceRepository;
    private final AuthRepository authRepository;

    public WalletServiceImpl(WalletBalanceRepository walletBalanceRepository, AuthRepository authRepository) {
        this.walletBalanceRepository = walletBalanceRepository;
        this.authRepository = authRepository;
    }

    @Override
    public List<WalletBalance> getBalancesForClient(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7).trim();
        BigInteger authToken;
        try {
            authToken = new BigInteger(token);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Invalid token format");
        }

        Optional<Auth> auth = authRepository.findByAuthToken(authToken);
        if (auth.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }

        return walletBalanceRepository.findByClientId(auth.get().getClientId());
    }
}
