package com.bridgelabz.cryptotracker;



import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.repository.PortfolioRepository;
import com.bridgelabz.cryptotracker.user.service.PortfolioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {

    @InjectMocks
    private PortfolioService portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPortfolioByUserId() {
        List<PortfolioEntry> entries = List.of(new PortfolioEntry());
        when(portfolioRepository.findByUserId("user123")).thenReturn(entries);

        List<PortfolioEntry> result = portfolioService.getPortfolioByUserId("user123");

        assertEquals(1, result.size());
        verify(portfolioRepository, times(1)).findByUserId("user123");
    }

    @Test
    void testAddPortfolioEntry_Success() {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setId(1);
        entry.setUserId("user123");
        entry.setCoinId("bitcoin");

        PortfolioService.CoinGeckoResponse[] apiResponse = new PortfolioService.CoinGeckoResponse[1];
        PortfolioService.CoinGeckoResponse coin = new PortfolioService.CoinGeckoResponse();
        coin.setName("Bitcoin");
        coin.setSymbol("BTC");
        coin.setCurrent_price(30000.0);
        apiResponse[0] = coin;

        when(restTemplate.getForObject(anyString(), eq(PortfolioService.CoinGeckoResponse[].class)))
                .thenReturn(apiResponse);

        when(portfolioRepository.save(any(PortfolioEntry.class))).thenReturn(entry);

        PortfolioEntry result = portfolioService.addPortfolioEntry(entry);

        assertEquals("Bitcoin", result.getCoinName());
        assertEquals("BTC", result.getSymbol());
        assertEquals(30000.0, result.getBuyPrice());
    }

    @Test
    void testUpdatePortfolioEntryQuantity_Success() {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setId(1);
        entry.setQuantityHeld(1.0);

        when(portfolioRepository.findById(1)).thenReturn(Optional.of(entry));
        when(portfolioRepository.save(any())).thenReturn(entry);

        PortfolioEntry result = portfolioService.updatePortfolioEntryQuantity(1, 2.5);

        assertEquals(2.5, result.getQuantityHeld());
    }

    @Test
    void testUpdatePortfolioEntryQuantity_NotFound() {
        when(portfolioRepository.findById(99)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                portfolioService.updatePortfolioEntryQuantity(99, 1.0));

        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testDeletePortfolioEntry() {
        portfolioService.deletePortfolioEntry(1);
        verify(portfolioRepository, times(1)).deleteById(1);
    }
}

