package com.bridgelabz.cryptotracker.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.repository.CryptoAssetRepository;

import java.util.List;
import java.util.Map;

@Service
public class CryptoAssetService {

    @Autowired
    private CryptoAssetRepository repository;

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&x_cg_demo_api_key=CG-uZ358azkg7dAiwGwHBuwrR46";

    public void updatePrices() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> prices = response.getBody();

        if (prices == null) return;

        List<CryptoAsset> assets = repository.findAll();

        for (CryptoAsset asset : assets) {
            prices.stream()
                .filter(p -> asset.getSymbol().equalsIgnoreCase((String) p.get("symbol")))
                .findFirst()
                .ifPresent(p -> {
                    Object priceObj = p.get("current_price");
                    if(priceObj != null) {
                        asset.setCurrentPrice(Double.parseDouble(priceObj.toString()));
                    }
                    Object lastUpdatedObj = p.get("last_updated");
                    if(lastUpdatedObj != null) {
                        asset.setLastUpdated(lastUpdatedObj.toString());
                    }
                });
        }

        repository.saveAll(assets);
    }

    public List<CryptoAsset> getPortfolio() {
        return repository.findAll();
    }
    public CryptoAsset saveAsset(CryptoAsset asset) {
        return repository.save(asset);
    }
}
