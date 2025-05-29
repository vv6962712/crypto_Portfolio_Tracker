package com.bridgelabz.cryptotracker.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.service.CryptoAssetService;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class CryptoAssetController {

    @Autowired
    private CryptoAssetService service;

    // Endpoint to refresh/update crypto prices from CoinGecko
    @GetMapping("/refresh")
    public String refreshPrices() {
        service.updatePrices();
        return "Crypto prices updated successfully!";
    }

    // Get all crypto assets in portfolio with current prices and PnL
    @GetMapping
    public List<CryptoAsset> getPortfolio() {
        return service.getPortfolio();
    }

    // Optional: Add a new crypto asset to portfolio
    @PostMapping("/api")
    public CryptoAsset addAsset(@RequestBody CryptoAsset asset) {
        // Check if asset with symbol already exists
        return service.getPortfolio()
                      .stream()
                      .filter(a -> a.getSymbol().equalsIgnoreCase(asset.getSymbol()))
                      .findFirst()
                      .orElseGet(() -> service.saveAsset(asset));  // use service.saveAsset() here
    }

}
