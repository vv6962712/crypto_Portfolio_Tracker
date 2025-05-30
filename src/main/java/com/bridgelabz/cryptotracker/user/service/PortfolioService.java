package com.bridgelabz.cryptotracker.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.cryptotracker.user.Interface.PortfolioServiceInterface;
import com.bridgelabz.cryptotracker.user.dto.PortfolioEntryDTO;
import com.bridgelabz.cryptotracker.user.dto.PortfolioMapper;
import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.repository.PortfolioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService implements PortfolioServiceInterface {

   @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String COINGECKO_API_URL =
            "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s&order=market_cap_desc&per_page=1&page=1";

    @Override
    public List<PortfolioEntryDTO> getPortfolioByUserId(String userId) {
        List<PortfolioEntry> entries = portfolioRepository.findByUserId(userId);
        return entries.stream()
                .map(PortfolioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PortfolioEntryDTO addPortfolioEntry(PortfolioEntryDTO dto) {
        String url = String.format(COINGECKO_API_URL, dto.getCoinId());

        CoinGeckoResponse[] response = restTemplate.getForObject(url, CoinGeckoResponse[].class);
        if (response == null || response.length == 0) {
            throw new RuntimeException("Coin not found in CoinGecko API");
        }

        CoinGeckoResponse coin = response[0];

        dto.setCoinName(coin.getName());
        dto.setSymbol(coin.getSymbol());
        dto.setBuyPrice(coin.getCurrent_price());

        PortfolioEntry entity = PortfolioMapper.toEntity(dto);
        PortfolioEntry saved = portfolioRepository.save(entity);

        return PortfolioMapper.toDTO(saved);
    }

    @Override
    public PortfolioEntryDTO updatePortfolioEntryQuantity(int id, Double quantityHeld) {
        Optional<PortfolioEntry> optionalEntry = portfolioRepository.findById(id);
        if (optionalEntry.isPresent()) {
            PortfolioEntry entry = optionalEntry.get();
            entry.setQuantityHeld(quantityHeld);
            PortfolioEntry updated = portfolioRepository.save(entry);
            return PortfolioMapper.toDTO(updated);
        } else {
            throw new RuntimeException("Portfolio entry not found with id " + id);
        }
    }

    @Override
    public void deletePortfolioEntry(int id) {
        portfolioRepository.deleteById(id);
    }

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
