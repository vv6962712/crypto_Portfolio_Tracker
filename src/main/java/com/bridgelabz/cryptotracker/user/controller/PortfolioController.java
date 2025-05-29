package com.bridgelabz.cryptotracker.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.service.PortfolioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Get all portfolio entries for a user
    @GetMapping("/{userId}")
    public List<PortfolioEntry> getPortfolioByUserId(@PathVariable String userId) {
        return portfolioService.getPortfolioByUserId(userId);
    }

    // Add new portfolio entry (id is required and must be unique)
    @PostMapping
    public ResponseEntity<PortfolioEntry> addPortfolioEntry(@RequestBody PortfolioEntry entry) {
        PortfolioEntry saved = portfolioService.addPortfolioEntry(entry);
        return ResponseEntity.ok(saved);
    }

    // Update quantity held for a portfolio entry by id
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioEntry> updateQuantity(
            @PathVariable int id,
            @RequestBody QuantityUpdateRequest request) {

        PortfolioEntry updated = portfolioService.updatePortfolioEntryQuantity(id, request.getQuantityHeld());
        return ResponseEntity.ok(updated);
    }

    // Delete portfolio entry by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolioEntry(@PathVariable int id) {
        portfolioService.deletePortfolioEntry(id);
        return ResponseEntity.ok("Deleted portfolio entry with id " + id);
    }

    // DTO for update quantity
    public static class QuantityUpdateRequest {
        private Double quantityHeld;

        public Double getQuantityHeld() {
            return quantityHeld;
        }

        public void setQuantityHeld(Double quantityHeld) {
            this.quantityHeld = quantityHeld;
        }
    }
}
