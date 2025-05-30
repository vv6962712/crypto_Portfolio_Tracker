package com.bridgelabz.cryptotracker.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bridgelabz.cryptotracker.user.dto.PortfolioEntryDTO;
import com.bridgelabz.cryptotracker.user.dto.PortfolioMapper;
import com.bridgelabz.cryptotracker.user.service.PortfolioService;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.bridgelabz.cryptotracker.user.exception.PortfolioEntryNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@Tag(name="Portfolio Management")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    
    @GetMapping("/{userId}")
    public List<PortfolioEntryDTO> getPortfolioByUserId(@PathVariable String userId) {
        return portfolioService.getPortfolioByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<PortfolioEntryDTO> addPortfolioEntry(@RequestBody PortfolioEntryDTO entryDTO) {
        PortfolioEntryDTO saved = portfolioService.addPortfolioEntry(entryDTO);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioEntryDTO> updateQuantity(
            @PathVariable int id,
            @RequestBody QuantityUpdateRequest request) {

        PortfolioEntryDTO updated = portfolioService.updatePortfolioEntryQuantity(id, request.getQuantityHeld());

        if (updated == null) {
            throw new PortfolioEntryNotFoundException(id);
        }
        return ResponseEntity.ok(updated);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolioEntry(@PathVariable int id) {
        portfolioService.deletePortfolioEntry(id);
        return ResponseEntity.ok("Deleted portfolio entry with id " + id);
    }


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
