package com.bridgelabz.cryptotracker.user.exception;

public class PortfolioEntryNotFoundException extends RuntimeException {
    public PortfolioEntryNotFoundException(int id) {
        super("Portfolio entry not found with id: " + id);
    }
}