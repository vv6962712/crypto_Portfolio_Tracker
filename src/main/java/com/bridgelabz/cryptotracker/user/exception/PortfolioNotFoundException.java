package com.bridgelabz.cryptotracker.user.exception;

public class PortfolioNotFoundException extends RuntimeException{
    public PortfolioNotFoundException(String message){
        super(message);
    }
}
