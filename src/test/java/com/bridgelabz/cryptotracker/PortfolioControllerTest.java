package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.config.SecurityConfig;
import com.bridgelabz.cryptotracker.user.controller.PortfolioController;
import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortfolioController.class)
@Import(SecurityConfig.class)
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPortfolioByUserId() throws Exception {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setUserId("user123");
        entry.setId(1);
        entry.setSymbol("btc");
        entry.setQuantityHeld(2.0);

        when(portfolioService.getPortfolioByUserId("user123")).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/portfolio/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }

    @Test
    void testAddPortfolioEntry() throws Exception {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setId(1);
        entry.setUserId("user123");
        entry.setSymbol("eth");
        entry.setQuantityHeld(5.0);

        when(portfolioService.addPortfolioEntry(Mockito.any())).thenReturn(entry);

        mockMvc.perform(post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("eth"));
    }

    @Test
    void testUpdateQuantity() throws Exception {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setId(1);
        entry.setUserId("user123");
        entry.setSymbol("btc");
        entry.setQuantityHeld(3.5);

        PortfolioController.QuantityUpdateRequest updateRequest = new PortfolioController.QuantityUpdateRequest();
        updateRequest.setQuantityHeld(3.5);

        when(portfolioService.updatePortfolioEntryQuantity(1, 3.5)).thenReturn(entry);

        mockMvc.perform(put("/api/portfolio/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityHeld").value(3.5));
    }

    @Test
    void testDeletePortfolioEntry() throws Exception {
        mockMvc.perform(delete("/api/portfolio/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted portfolio entry with id 1"));
    }
}
