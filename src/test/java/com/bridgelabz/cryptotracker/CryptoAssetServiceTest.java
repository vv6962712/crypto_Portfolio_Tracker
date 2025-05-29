package com.bridgelabz.cryptotracker;



import com.bridgelabz.cryptotracker.user.entity.CryptoAsset;
import com.bridgelabz.cryptotracker.user.repository.CryptoAssetRepository;
import com.bridgelabz.cryptotracker.user.service.CryptoAssetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CryptoAssetServiceTest {

    @InjectMocks
    private CryptoAssetService service;

    @Mock
    private CryptoAssetRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPortfolio() {
        List<CryptoAsset> mockAssets = List.of(
                new CryptoAsset() {{
                    setSymbol("btc");
                    setQuantityHeld(1);
                    setBuyPrice(30000);
                    setCurrentPrice(35000);
                }}
        );
        when(repository.findAll()).thenReturn(mockAssets);

        List<CryptoAsset> result = service.getPortfolio();

        assertEquals(1, result.size());
        assertEquals("btc", result.get(0).getSymbol());
    }

    @Test
    void testSaveAsset() {
        CryptoAsset asset = new CryptoAsset();
        asset.setSymbol("eth");
        when(repository.save(asset)).thenReturn(asset);

        CryptoAsset saved = service.saveAsset(asset);

        assertEquals("eth", saved.getSymbol());
    }

}

