package com.bridgelabz.cryptotracker.user.Interface;

import com.bridgelabz.cryptotracker.user.dto.CryptoAssetDTO;

import java.util.List;

public interface CryptoAssetServiceInterface {
    void updatePrices();
    List<CryptoAssetDTO> getPortfolio();
    CryptoAssetDTO saveAsset(CryptoAssetDTO dto);
}
