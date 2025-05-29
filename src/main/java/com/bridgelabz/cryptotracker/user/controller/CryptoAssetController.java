package com.bridgelabz.cryptotracker.user.controller;

import com.bridgelabz.cryptotracker.user.dto.CryptoAssetDTO;
import com.bridgelabz.cryptotracker.user.service.CryptoAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bridgelabz.cryptotracker.user.exception.PortfolioNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class CryptoAssetController {

    @Autowired
    private CryptoAssetService service;

    @GetMapping("/refresh")
    public String refreshPrices() {
        service.updatePrices();
        return "Crypto prices updated successfully!";
    }

    @GetMapping
    public List<CryptoAssetDTO> getPortfolio() {
        List<CryptoAssetDTO> portfolioDTO = service.getPortfolio();
        if (portfolioDTO.isEmpty() || portfolioDTO.stream().allMatch(dto -> dto.getQuantityHeld() == 0.0)) {
            throw new PortfolioNotFoundException("No valid assets found in the portfolio.");
        }
        return portfolioDTO;
    }

    @PostMapping("/api")
    public CryptoAssetDTO addAsset(@RequestBody CryptoAssetDTO assetDto) {
        return service.getPortfolio().stream()
                .filter(a -> a.getSymbol().equalsIgnoreCase(assetDto.getSymbol()))
                .findFirst()
                .orElseGet(() -> service.saveAsset(assetDto));
    }
}
