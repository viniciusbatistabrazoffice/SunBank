package com.backend.repository;

import com.backend.entity.WalletBalance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletBalanceRepository extends CrudRepository<WalletBalance, Long> {
    List<WalletBalance> findByClientId(Long clientId);
}
