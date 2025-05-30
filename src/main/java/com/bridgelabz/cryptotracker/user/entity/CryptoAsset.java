package com.bridgelabz.cryptotracker.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class CryptoAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private double quantityHeld;
    private double buyPrice;
    private double currentPrice;
    private String lastUpdated;

    public double getCurrentValue() {
        return quantityHeld * currentPrice;
    }

    public double getPnL() {
        return getCurrentValue() - buyPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getQuantityHeld() { return quantityHeld; }
    public void setQuantityHeld(double quantityHeld) { this.quantityHeld = quantityHeld; }

    public double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(double buyPrice) { this.buyPrice = buyPrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}
