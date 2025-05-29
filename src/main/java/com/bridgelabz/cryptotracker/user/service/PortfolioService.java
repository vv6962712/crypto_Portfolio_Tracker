package com.bridgelabz.cryptotracker.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.repository.PortfolioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String COINGECKO_API_URL =
            "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s&order=market_cap_desc&per_page=1&page=1";

    public List<PortfolioEntry> getPortfolioByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public PortfolioEntry addPortfolioEntry(PortfolioEntry entry) {
        // Fetch coin data from CoinGecko API to fill coinName, symbol, buyPrice (current_price)
        String url = String.format(COINGECKO_API_URL, entry.getCoinId());

        CoinGeckoResponse[] response = restTemplate.getForObject(url, CoinGeckoResponse[].class);
        if (response == null || response.length == 0) {
            throw new RuntimeException("Coin not found in CoinGecko API");
        }

        CoinGeckoResponse coin = response[0];

        entry.setCoinName(coin.getName());
        entry.setSymbol(coin.getSymbol());
        entry.setBuyPrice(coin.getCurrent_price());

        // Save entry to DB (id must be manually set by caller)
        return portfolioRepository.save(entry);
    }

    public PortfolioEntry updatePortfolioEntryQuantity(int id, Double quantityHeld) {
        Optional<PortfolioEntry> optionalEntry = portfolioRepository.findById(id);
        if (optionalEntry.isPresent()) {
            PortfolioEntry entry = optionalEntry.get();
            entry.setQuantityHeld(quantityHeld);
            return portfolioRepository.save(entry);
        } else {
            throw new RuntimeException("Portfolio entry not found with id " + id);
        }
    }

    public void deletePortfolioEntry(int id) {
        portfolioRepository.deleteById(id);
    }

    // DTO class to map API response (only needed fields)
    public static class CoinGeckoResponse {
        private String id;
        private String symbol;
        private String name;
        private Double current_price;

        // getters and setters

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getCurrent_price() { return current_price; }
        public void setCurrent_price(Double current_price) { this.current_price = current_price; }
    }
}
