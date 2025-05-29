package com.bridgelabz.cryptotracker;




import com.bridgelabz.cryptotracker.config.SecurityConfig;
import com.bridgelabz.cryptotracker.user.controller.CryptoAssetController;
import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.service.CryptoAssetService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CryptoAssetController.class)
@Import(SecurityConfig.class)
public class CryptoAssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoAssetService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRefreshPrices() throws Exception {
        mockMvc.perform(get("/api/portfolio/refresh"))
                .andExpect(status().isOk())
                .andExpect(content().string("Crypto prices updated successfully!"));
    }

    @Test
    void testGetPortfolio() throws Exception {
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol("btc");
        asset.setQuantityHeld(1.0);
        asset.setBuyPrice(25000);
        asset.setCurrentPrice(30000);

        Mockito.when(service.getPortfolio()).thenReturn(List.of(asset));

        mockMvc.perform(get("/api/portfolio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("btc"));
    }

    @Test
    void testAddAsset_NewAsset() throws Exception {
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol("eth");
        asset.setQuantityHeld(2.0);
        asset.setBuyPrice(2000);

        Mockito.when(service.getPortfolio()).thenReturn(List.of());
        Mockito.when(service.saveAsset(Mockito.any())).thenReturn(asset);

        mockMvc.perform(post("/api/portfolio/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("eth"));
    }
}
