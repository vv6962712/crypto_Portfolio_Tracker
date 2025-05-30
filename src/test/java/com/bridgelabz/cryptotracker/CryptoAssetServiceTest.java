package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.user.dto.CryptoAssetDTO;
import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.service.CryptoAssetService;
import com.bridgelabz.cryptotracker.user.repository.CryptoAssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CryptoAssetServiceTest {

    private CryptoAssetRepository repository;
    private CryptoAssetService service;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        repository = mock(CryptoAssetRepository.class);
        restTemplate = mock(RestTemplate.class);
        service = new CryptoAssetService();

        
        try {
            var repoField = CryptoAssetService.class.getDeclaredField("repository");
            repoField.setAccessible(true);
            repoField.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetPortfolio() {
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol("btc");
        asset.setBuyPrice(1000);
        asset.setQuantityHeld(2);
        asset.setCurrentPrice(1500);
        asset.setLastUpdated("2025-05-29");

        when(repository.findAll()).thenReturn(List.of(asset));

        var result = service.getPortfolio();

        assertEquals(1, result.size());
        CryptoAssetDTO dto = result.get(0);
        assertEquals("btc", dto.getSymbol());
        assertEquals(1000, dto.getBuyPrice());
        assertEquals(2, dto.getQuantityHeld());
        assertEquals(1500, dto.getCurrentPrice());
        assertEquals("2025-05-29", dto.getLastUpdated());
        assertEquals(3000, dto.getCurrentValue());
        assertEquals(2000, dto.getPnl());
    }

    @Test
    void testSaveAsset() {
        CryptoAssetDTO dto = new CryptoAssetDTO();
        dto.setSymbol("eth");
        dto.setBuyPrice(2000);
        dto.setQuantityHeld(3);
        dto.setCurrentPrice(2200);
        dto.setLastUpdated("2025-05-29");

        CryptoAsset savedEntity = new CryptoAsset();
        savedEntity.setSymbol(dto.getSymbol());
        savedEntity.setBuyPrice(dto.getBuyPrice());
        savedEntity.setQuantityHeld(dto.getQuantityHeld());
        savedEntity.setCurrentPrice(dto.getCurrentPrice());
        savedEntity.setLastUpdated(dto.getLastUpdated());

        when(repository.save(any(CryptoAsset.class))).thenReturn(savedEntity);

        CryptoAssetDTO result = service.saveAsset(dto);

        assertEquals(dto.getSymbol(), result.getSymbol());
        assertEquals(dto.getBuyPrice(), result.getBuyPrice());
        assertEquals(dto.getQuantityHeld(), result.getQuantityHeld());
        assertEquals(dto.getCurrentPrice(), result.getCurrentPrice());
        assertEquals(dto.getLastUpdated(), result.getLastUpdated());

        verify(repository, times(1)).save(any(CryptoAsset.class));
    }
    
    @Test
    void testUpdatePrices_withSpy() {
        CryptoAssetService spyService = spy(service);

        
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol("btc");
        asset.setCurrentPrice(25000);
        when(repository.findAll()).thenReturn(List.of(asset));
        when(repository.saveAll(any())).thenReturn(List.of(asset));

        
        spyService.updatePrices();

        
        verify(repository, times(1)).saveAll(any());
    }

}
