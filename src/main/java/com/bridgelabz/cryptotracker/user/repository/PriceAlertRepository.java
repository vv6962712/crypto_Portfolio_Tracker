package com.bridgelabz.cryptotracker.user.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.cryptotracker.user.entity.PriceAlert;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByStatus(String status);
    List<PriceAlert> findAll();
}

