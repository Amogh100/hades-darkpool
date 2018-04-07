package application.core.services;

import application.core.repositories.TradeRepository;
import models.entities.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//Rest Service for obtaining trade information
//Meant for use in Controllers, so that controllers don't directly speak to the database layer.
@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> getTradesForTrader(long traderId){
        return tradeRepository.findByTraderId(traderId);
    }
}
