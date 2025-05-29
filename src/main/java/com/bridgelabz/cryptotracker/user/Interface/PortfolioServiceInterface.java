package com.bridgelabz.cryptotracker.user.Interface;



import com.bridgelabz.cryptotracker.user.dto.PortfolioEntryDTO;

import java.util.List;

public interface PortfolioServiceInterface {

    List<PortfolioEntryDTO> getPortfolioByUserId(String userId);

    PortfolioEntryDTO addPortfolioEntry(PortfolioEntryDTO dto);

    PortfolioEntryDTO updatePortfolioEntryQuantity(int id, Double quantityHeld);

    void deletePortfolioEntry(int id);
}
