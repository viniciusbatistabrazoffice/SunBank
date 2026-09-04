package com.backend.service;

import com.backend.entity.Auth;
import com.backend.entity.VirtualCard;
import com.backend.repository.AuthRepository;
import com.backend.repository.VirtualCardRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VirtualCardServiceImpl implements VirtualCardService {

    private final VirtualCardRepository virtualCardRepository;
    private final AuthRepository authRepository;

    public VirtualCardServiceImpl(VirtualCardRepository virtualCardRepository, AuthRepository authRepository) {
        this.virtualCardRepository = virtualCardRepository;
        this.authRepository = authRepository;
    }

    @Override
    public List<VirtualCard> getCardsForClient(String authHeader) {
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

        List<VirtualCard> cards = virtualCardRepository.findByClientId(auth.get().getClientId());
        if (cards.isEmpty()) {
            return new ArrayList<>();
        }
        return cards;
    }
}
