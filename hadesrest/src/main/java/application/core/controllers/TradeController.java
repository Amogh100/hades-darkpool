package application.core.controllers;

import application.core.repositories.TraderRepository;
import application.core.services.TradeService;
import models.entities.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TraderRepository traderRepository;

    @GetMapping(value="/trades", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Trade>> getOrdersForTrader(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Long traderId = traderRepository.findByUsername(userName).getId();
        return ResponseEntity.ok().body(tradeService.getTradesForTrader(traderId));
    }
}
