package com.bridgelabz.cryptotracker.user.dto;

import com.bridgelabz.cryptotracker.user.entity.PortfolioEntry;

public class PortfolioMapper {

    public static PortfolioEntryDTO toDTO(PortfolioEntry entity) {
        PortfolioEntryDTO dto = new PortfolioEntryDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCoinId(entity.getCoinId());
        dto.setCoinName(entity.getCoinName());
        dto.setSymbol(entity.getSymbol());
        dto.setQuantityHeld(entity.getQuantityHeld());
        dto.setBuyPrice(entity.getBuyPrice());
        dto.setBuyDate(entity.getBuyDate());
        return dto;
    }

    public static PortfolioEntry toEntity(PortfolioEntryDTO dto) {
        PortfolioEntry entity = new PortfolioEntry();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setCoinId(dto.getCoinId());
        entity.setCoinName(dto.getCoinName());
        entity.setSymbol(dto.getSymbol());
        entity.setQuantityHeld(dto.getQuantityHeld());
        entity.setBuyPrice(dto.getBuyPrice());
        entity.setBuyDate(dto.getBuyDate());
        return entity;
    }
}
