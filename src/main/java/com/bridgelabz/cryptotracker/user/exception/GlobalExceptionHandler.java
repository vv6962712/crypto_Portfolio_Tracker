package com.bridgelabz.cryptotracker.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // handles custom UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handles custom PriceAlertNotFoundException
    @ExceptionHandler(PriceAlertNotFoundException.class)
    public ResponseEntity<String> handlePriceAlertNotFound(PriceAlertNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handles custom PortfolioNotFoundException
    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<String> handlePortfolioNotFoundException(PortfolioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handles custom PortfolioEntryNotFoundException
    @ExceptionHandler(PortfolioEntryNotFoundException.class)
    public ResponseEntity<String> handlePortfolioEntryNotFound(PortfolioEntryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handles validation errors like @Valid in controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // handles generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}

