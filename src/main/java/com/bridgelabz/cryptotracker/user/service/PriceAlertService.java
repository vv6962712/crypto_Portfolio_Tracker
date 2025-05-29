package com.bridgelabz.cryptotracker.user.service;



import com.bridgelabz.cryptotracker.user.entity.PriceAlert;
import com.bridgelabz.cryptotracker.user.repository.PriceAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAlertService {
    private final PriceAlertRepository repo;

    public PriceAlertService(PriceAlertRepository repo) {
        this.repo = repo;
    }

    public List<PriceAlert> getAll() {
        return repo.findAll();
    }

    public void save(PriceAlert alert) {
        repo.save(alert);
    }

    public List<PriceAlert> getTriggered() {
        return repo.findByStatus("triggered");
    }
}

