package com.backend.service;

import com.backend.entity.VirtualCard;

import java.util.List;

public interface VirtualCardService {
    List<VirtualCard> getCardsForClient(String authHeader);
}
