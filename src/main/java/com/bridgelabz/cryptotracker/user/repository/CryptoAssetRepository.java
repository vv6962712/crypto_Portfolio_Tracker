package com.bridgelabz.cryptotracker.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;

public interface CryptoAssetRepository extends JpaRepository<CryptoAsset, Long> {}
