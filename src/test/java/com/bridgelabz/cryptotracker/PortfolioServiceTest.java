package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.user.dto.PortfolioEntryDTO;
import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;
import com.bridgelabz.cryptotracker.user.repository.PortfolioRepository;
import com.bridgelabz.cryptotracker.user.service.PortfolioService;
import com.bridgelabz.cryptotracker.user.service.PortfolioService.CoinGeckoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

    private PortfolioRepository portfolioRepository;
    private RestTemplate restTemplate;
    private PortfolioService portfolioService;

    @BeforeEach
    void setup() {
        portfolioRepository = mock(PortfolioRepository.class);
        restTemplate = mock(RestTemplate.class);
        portfolioService = new PortfolioService();

        
        ReflectionTestUtils.setField(portfolioService, "portfolioRepository", portfolioRepository);
        ReflectionTestUtils.setField(portfolioService, "restTemplate", restTemplate);
    }

    @Test
    void testGetPortfolioByUserId() {
        PortfolioEntry entry = new PortfolioEntry();
        entry.setId(1);
        entry.setUserId("user1");
        entry.setCoinId("bitcoin");
        entry.setCoinName("Bitcoin");
        entry.setSymbol("btc");
        entry.setQuantityHeld(1.5);
        entry.setBuyPrice(20000.0);
        entry.setBuyDate(LocalDate.of(2023, 5, 29));

        when(portfolioRepository.findByUserId("user1")).thenReturn(List.of(entry));

        List<PortfolioEntryDTO> dtos = portfolioService.getPortfolioByUserId("user1");

        assertEquals(1, dtos.size());
        assertEquals("Bitcoin", dtos.get(0).getCoinName());
    }

    @Test
    void testAddPortfolioEntry_Success() {
        PortfolioEntryDTO dto = new PortfolioEntryDTO();
        dto.setId(2);
        dto.setUserId("user2");
        dto.setCoinId("ethereum");

        CoinGeckoResponse mockResponse = new CoinGeckoResponse();
        mockResponse.setId("ethereum");
        mockResponse.setName("Ethereum");
        mockResponse.setSymbol("eth");
        mockResponse.setCurrent_price(1500.0);

        when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponse[].class)))
                .thenReturn(new CoinGeckoResponse[]{mockResponse});

        when(portfolioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PortfolioEntryDTO savedDto = portfolioService.addPortfolioEntry(dto);

        assertEquals("Ethereum", savedDto.getCoinName());
        assertEquals("eth", savedDto.getSymbol());
        assertEquals(1500.0, savedDto.getBuyPrice());
    }

    @Test
    void testAddPortfolioEntry_CoinNotFound_Throws() {
        PortfolioEntryDTO dto = new PortfolioEntryDTO();
        dto.setId(3);
        dto.setUserId("user3");
        dto.setCoinId("nonexistent");

        when(restTemplate.getForObject(anyString(), eq(CoinGeckoResponse[].class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> portfolioService.addPortfolioEntry(dto));
        assertEquals("Coin not found in CoinGecko API", exception.getMessage());
    }

    @Test
    void testUpdatePortfolioEntryQuantity_Success() {
        PortfolioEntry existing = new PortfolioEntry();
        existing.setId(4);
        existing.setQuantityHeld(2.0);

        when(portfolioRepository.findById(4)).thenReturn(Optional.of(existing));
        when(portfolioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PortfolioEntryDTO updatedDto = portfolioService.updatePortfolioEntryQuantity(4, 5.5);

        assertEquals(5.5, updatedDto.getQuantityHeld());
    }

    @Test
    void testUpdatePortfolioEntryQuantity_NotFound() {
        when(portfolioRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> portfolioService.updatePortfolioEntryQuantity(999, 1.0));

        assertEquals("Portfolio entry not found with id 999", exception.getMessage());
    }

    @Test
    void testDeletePortfolioEntry() {
        doNothing().when(portfolioRepository).deleteById(5);

        portfolioService.deletePortfolioEntry(5);

        verify(portfolioRepository, times(1)).deleteById(5);
    }
}
