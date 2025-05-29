package com.bridgelabz.cryptotracker.user.scheduler;



import com.bridgelabz.cryptotracker.user.entity.PriceAlert;
import com.bridgelabz.cryptotracker.user.service.PriceAlertService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class PriceAlertScheduler {

    private final PriceAlertService alertService;
    private final RestTemplate restTemplate = new RestTemplate();

    public PriceAlertScheduler(PriceAlertService alertService) {
        this.alertService = alertService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAlerts() throws Exception {
        for (PriceAlert alert : alertService.getAll()) {
            if ("triggered".equalsIgnoreCase(alert.getStatus())) continue;

            String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s", alert.getCoinId());
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = new ObjectMapper().readTree(response).get(0);
            double currentPrice = jsonNode.get("current_price").asDouble();

            boolean conditionMet = ("above".equals(alert.getDirection()) && currentPrice > alert.getTriggerPrice()) ||
                                   ("below".equals(alert.getDirection()) && currentPrice < alert.getTriggerPrice());

            if (conditionMet) {
                alert.setStatus("triggered");
                alert.setTriggeredAt(LocalDateTime.now());
                alertService.save(alert);
                System.out.println("Alert triggered for: " + alert.getSymbol());
            }
        }
    }
}

