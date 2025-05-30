package com.bridgelabz.cryptotracker.user.service;

import com.bridgelabz.cryptotracker.user.Interface.PriceAlertServiceInterface;
import com.bridgelabz.cryptotracker.user.dto.PriceAlertDTO;
import com.bridgelabz.cryptotracker.user.entity.PriceAlert;
import com.bridgelabz.cryptotracker.user.repository.PriceAlertRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAlertService implements PriceAlertServiceInterface {
    @Autowired
    private final PriceAlertRepository repo;

    public PriceAlertService(PriceAlertRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<PriceAlertDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void save(PriceAlertDTO dto) {
        PriceAlert entity = fromDTO(dto);
        repo.save(entity);
    }

    @Override
    public List<PriceAlertDTO> getTriggered() {
        return repo.findByStatus("triggered").stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PriceAlertDTO toDTO(PriceAlert alert) {
        PriceAlertDTO dto = new PriceAlertDTO();
        dto.setId(alert.getId());
        dto.setUserId(alert.getUserId());
        dto.setCoinId(alert.getCoinId());
        dto.setSymbol(alert.getSymbol());
        dto.setTriggerPrice(alert.getTriggerPrice());
        dto.setDirection(alert.getDirection());
        dto.setStatus(alert.getStatus());
        dto.setTriggeredAt(alert.getTriggeredAt());
        return dto;
    }

    @Override
    public PriceAlert fromDTO(PriceAlertDTO dto) {
        PriceAlert alert = new PriceAlert();
        alert.setId(dto.getId());
        alert.setUserId(dto.getUserId());
        alert.setCoinId(dto.getCoinId());
        alert.setSymbol(dto.getSymbol());
        alert.setTriggerPrice(dto.getTriggerPrice());
        alert.setDirection(dto.getDirection());
        alert.setStatus(dto.getStatus());
        alert.setTriggeredAt(dto.getTriggeredAt());
        return alert;
    }
}
