package com.bridgelabz.cryptotracker.user.controller;

import com.bridgelabz.cryptotracker.user.dto.LoginRequest;
import com.bridgelabz.cryptotracker.user.dto.RegisterRequest;
import com.bridgelabz.cryptotracker.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.TableGenerator;
import jakarta.validation.Valid;  
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name="Registration And User")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(
            request.getName(),
            request.getUserId(),
            request.getEmail(),
            request.getPassword(),
            request.getRole()
        );
        return ResponseEntity.ok("Registered successfully");
    }

   
    @GetMapping("/users")
    public List<?> getAllUsers() {
        return userService.getAllUsers();
    }

 
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            String message = userService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }
}
