package com.bridgelabz.cryptotracker.user.controller;



import com.bridgelabz.cryptotracker.user.entity.PriceAlert;
import com.bridgelabz.cryptotracker.user.service.PriceAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class PriceAlertController {

    private final PriceAlertService service;

    public PriceAlertController(PriceAlertService service) {
        this.service = service;
    }

    @PostMapping
    public void addAlert(@RequestBody PriceAlert alert) {
        alert.setStatus("pending");
        service.save(alert);
    }

    @GetMapping
    public List<PriceAlert> all() {
        return service.getAll();
    }

    @GetMapping("/triggered")
    public List<PriceAlert> triggered() {
        return service.getTriggered();
    }
}
