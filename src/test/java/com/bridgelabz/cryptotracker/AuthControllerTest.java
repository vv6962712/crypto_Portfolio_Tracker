package com.bridgelabz.cryptotracker;


import com.bridgelabz.cryptotracker.user.controller.AuthController;
import com.bridgelabz.cryptotracker.user.dto.LoginRequest;
import com.bridgelabz.cryptotracker.user.dto.RegisterRequest;
import com.bridgelabz.cryptotracker.user.entity.User;
import com.bridgelabz.cryptotracker.user.service.UserService;
import com.bridgelabz.cryptotracker.config.SecurityConfig;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)  // Import the test security config to disable security for tests
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // To convert objects to/from JSON

    @org.springframework.boot.test.mock.mockito.MockBean
    private UserService userService;

    @Test
    void testRegister_success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("secure123");
        request.setRole("ROLE_USER");
        request.setUserId(1);

        // No exception from service means success
        Mockito.doNothing().when(userService).registerUser(
                anyString(), any(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registered successfully"));
    }

    @Test
    void testGetAllUsers_success() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testLogin_success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("bob@example.com");
        request.setPassword("pass123");

        when(userService.loginUser(anyString(), anyString()))
                .thenReturn("Logged in successfully as: Bob");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged in successfully as: Bob"));
    }

    @Test
    void testLogin_failure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("bob@example.com");
        request.setPassword("wrongpass");

        doThrow(new Exception("Invalid password"))
                .when(userService).loginUser(anyString(), anyString());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed: Invalid password"));
    }
}
