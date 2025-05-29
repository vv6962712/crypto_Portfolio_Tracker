package com.bridgelabz.cryptotracker.user.Interface;



import com.bridgelabz.cryptotracker.user.dto.PriceAlertDTO;

import java.util.List;

public interface PriceAlertServiceInterface {

    List<PriceAlertDTO> getAll();

    void save(PriceAlertDTO dto);

    List<PriceAlertDTO> getTriggered();

    PriceAlertDTO toDTO(com.bridgelabz.cryptotracker.user.entity.PriceAlert alert);

    com.bridgelabz.cryptotracker.user.entity.PriceAlert fromDTO(PriceAlertDTO dto);
}
