package com.bridgelabz.cryptotracker;

import com.bridgelabz.cryptotracker.user.dto.PriceAlertDTO;
import com.bridgelabz.cryptotracker.user.entity.PriceAlert;
import com.bridgelabz.cryptotracker.user.repository.PriceAlertRepository;
import com.bridgelabz.cryptotracker.user.service.PriceAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceAlertServiceTest {

    private PriceAlertRepository repo;
    private PriceAlertService service;

    @BeforeEach
    void setUp() {
        repo = mock(PriceAlertRepository.class);
        service = new PriceAlertService(repo);
    }

    @Test
    void testGetAll() {
        PriceAlert alert1 = new PriceAlert();
        alert1.setId(1L);
        alert1.setUserId("user1");
        alert1.setCoinId("btc");
        alert1.setSymbol("BTC");
        alert1.setTriggerPrice(50000);
        alert1.setDirection("above");
        alert1.setStatus("pending");

        PriceAlert alert2 = new PriceAlert();
        alert2.setId(2L);
        alert2.setUserId("user2");
        alert2.setCoinId("eth");
        alert2.setSymbol("ETH");
        alert2.setTriggerPrice(3000);
        alert2.setDirection("below");
        alert2.setStatus("triggered");

        when(repo.findAll()).thenReturn(List.of(alert1, alert2));

        List<PriceAlertDTO> dtos = service.getAll();

        assertEquals(2, dtos.size());

        PriceAlertDTO dto1 = dtos.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("user1", dto1.getUserId());
        assertEquals("above", dto1.getDirection());
        assertEquals("pending", dto1.getStatus());

        PriceAlertDTO dto2 = dtos.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("user2", dto2.getUserId());
        assertEquals("below", dto2.getDirection());
        assertEquals("triggered", dto2.getStatus());
    }

    @Test
    void testGetTriggered() {
        PriceAlert alert = new PriceAlert();
        alert.setId(10L);
        alert.setUserId("userX");
        alert.setCoinId("doge");
        alert.setSymbol("DOGE");
        alert.setTriggerPrice(0.5);
        alert.setDirection("above");
        alert.setStatus("triggered");
        alert.setTriggeredAt(LocalDateTime.now());

        when(repo.findByStatus("triggered")).thenReturn(List.of(alert));

        List<PriceAlertDTO> triggered = service.getTriggered();

        assertEquals(1, triggered.size());
        assertEquals("triggered", triggered.get(0).getStatus());
        assertNotNull(triggered.get(0).getTriggeredAt());
    }

    @Test
    void testSave() {
        PriceAlertDTO dto = new PriceAlertDTO();
        dto.setUserId("userSave");
        dto.setCoinId("ltc");
        dto.setSymbol("LTC");
        dto.setTriggerPrice(150);
        dto.setDirection("below");
        dto.setStatus("pending");

        service.save(dto);

        ArgumentCaptor<PriceAlert> captor = ArgumentCaptor.forClass(PriceAlert.class);
        verify(repo, times(1)).save(captor.capture());

        PriceAlert savedEntity = captor.getValue();

        assertEquals(dto.getUserId(), savedEntity.getUserId());
        assertEquals(dto.getCoinId(), savedEntity.getCoinId());
        assertEquals(dto.getDirection(), savedEntity.getDirection());
        assertEquals(dto.getStatus(), savedEntity.getStatus());
    }

    @Test
    void testToDTOAndFromDTO() {
        PriceAlert alert = new PriceAlert();
        alert.setId(100L);
        alert.setUserId("userTest");
        alert.setCoinId("xrp");
        alert.setSymbol("XRP");
        alert.setTriggerPrice(1.2);
        alert.setDirection("above");
        alert.setStatus("pending");
        alert.setTriggeredAt(LocalDateTime.now());

        PriceAlertDTO dto = service.toDTO(alert);

        assertEquals(alert.getId(), dto.getId());
        assertEquals(alert.getUserId(), dto.getUserId());
        assertEquals(alert.getDirection(), dto.getDirection());
        assertEquals(alert.getStatus(), dto.getStatus());

        PriceAlert convertedBack = service.fromDTO(dto);
        assertEquals(dto.getId(), convertedBack.getId());
        assertEquals(dto.getUserId(), convertedBack.getUserId());
        assertEquals(dto.getDirection(), convertedBack.getDirection());
        assertEquals(dto.getStatus(), convertedBack.getStatus());
    }
}
