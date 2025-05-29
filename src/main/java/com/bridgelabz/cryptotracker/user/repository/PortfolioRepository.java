package com.bridgelabz.cryptotracker.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioEntry, Integer> {
    List<PortfolioEntry> findByUserId(String userId);
}
