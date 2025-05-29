package com.bridgelabz.cryptotracker.user.dto;

import java.time.LocalDateTime;

public class PriceAlertDTO {
    private Long id;
    private String userId;
    private String coinId;
    private String symbol;
    private double triggerPrice;
    private String direction; // above / below
    private String status;    // pending / triggered
    private LocalDateTime triggeredAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCoinId() { return coinId; }
    public void setCoinId(String coinId) { this.coinId = coinId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getTriggerPrice() { return triggerPrice; }
    public void setTriggerPrice(double triggerPrice) { this.triggerPrice = triggerPrice; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }
}
