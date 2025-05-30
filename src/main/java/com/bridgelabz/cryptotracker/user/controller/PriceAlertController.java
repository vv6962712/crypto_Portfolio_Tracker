package com.bridgelabz.cryptotracker.user.controller;

import com.bridgelabz.cryptotracker.user.dto.PriceAlertDTO;
import com.bridgelabz.cryptotracker.user.service.PriceAlertService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@Tag(name="Price Alerts")
public class PriceAlertController {

    private final PriceAlertService service;

    public PriceAlertController(PriceAlertService service) {
        this.service = service;
    }

    @PostMapping
    public void addAlert(@RequestBody PriceAlertDTO alertDTO) {
        alertDTO.setStatus("pending");
        service.save(alertDTO);
    }

    @GetMapping
    public List<PriceAlertDTO> all() {
        return service.getAll();
    }

    @GetMapping("/triggered")
    public List<PriceAlertDTO> triggered() {
        return service.getTriggered();
    }
}
