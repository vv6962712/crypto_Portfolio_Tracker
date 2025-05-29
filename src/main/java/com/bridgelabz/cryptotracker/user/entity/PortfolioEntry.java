package com.bridgelabz.cryptotracker.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class PortfolioEntry {

    @Id
    private int id;  // manually assigned primary key

    private String userId;
    private String coinId;    // CoinGecko coin id e.g. "bitcoin"
    private String coinName;
    private String symbol;
    private Double quantityHeld;
    private Double buyPrice;
    private LocalDate buyDate;

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCoinId() { return coinId; }
    public void setCoinId(String coinId) { this.coinId = coinId; }

    public String getCoinName() { return coinName; }
    public void setCoinName(String coinName) { this.coinName = coinName; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Double getQuantityHeld() { return quantityHeld; }
    public void setQuantityHeld(Double quantityHeld) { this.quantityHeld = quantityHeld; }

    public Double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(Double buyPrice) { this.buyPrice = buyPrice; }

    public LocalDate getBuyDate() { return buyDate; }
    public void setBuyDate(LocalDate buyDate) { this.buyDate = buyDate; }
}
