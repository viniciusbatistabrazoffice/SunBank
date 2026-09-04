package com.backend.repository;

import com.backend.entity.VirtualCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VirtualCardRepository extends CrudRepository<VirtualCard, Long> {
    List<VirtualCard> findByClientId(Long clientId);
}
