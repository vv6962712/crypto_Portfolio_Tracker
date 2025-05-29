package com.bridgelabz.cryptotracker.user.service;

import com.bridgelabz.cryptotracker.user.Interface.CryptoAssetServiceInterface;
import com.bridgelabz.cryptotracker.user.dto.CryptoAssetDTO;
import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.repository.CryptoAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CryptoAssetService implements CryptoAssetServiceInterface {

    @Autowired
    private CryptoAssetRepository repository;

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&x_cg_demo_api_key=CG-uZ358azkg7dAiwGwHBuwrR46";

    @Override
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
                    if (priceObj != null) {
                        asset.setCurrentPrice(Double.parseDouble(priceObj.toString()));
                    }
                    Object lastUpdatedObj = p.get("last_updated");
                    if (lastUpdatedObj != null) {
                        asset.setLastUpdated(lastUpdatedObj.toString());
                    }
                });
        }

        repository.saveAll(assets);
    }

    @Override
    public List<CryptoAssetDTO> getPortfolio() {
        return repository.findAll()
                         .stream()
                         .map(this::convertToDTO)
                         .collect(Collectors.toList());
    }

    @Override
    public CryptoAssetDTO saveAsset(CryptoAssetDTO dto) {
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol(dto.getSymbol());
        asset.setBuyPrice(dto.getBuyPrice());
        asset.setQuantityHeld(dto.getQuantityHeld());
        asset.setCurrentPrice(dto.getCurrentPrice());
        asset.setLastUpdated(dto.getLastUpdated());

        CryptoAsset saved = repository.save(asset);
        return convertToDTO(saved);
    }

    private CryptoAssetDTO convertToDTO(CryptoAsset asset) {
        CryptoAssetDTO dto = new CryptoAssetDTO();
        dto.setSymbol(asset.getSymbol());
        dto.setBuyPrice(asset.getBuyPrice());
        dto.setQuantityHeld(asset.getQuantityHeld());
        dto.setCurrentPrice(asset.getCurrentPrice());
        dto.setLastUpdated(asset.getLastUpdated());
        dto.setCurrentValue(asset.getCurrentValue());
        dto.setPnl(asset.getPnL());
        return dto;
    }
}
