package com.bridgelabz.cryptotracker.user.dto;

public class CryptoAssetDTO {
    private String symbol;
    private double quantityHeld;
    private double buyPrice;
    private double currentPrice;
    private String lastUpdated;
    private double currentValue;
    private double pnl;

    // Getters and Setters
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

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public double getPnl() { return pnl; }
    public void setPnl(double pnl) { this.pnl = pnl; }
}