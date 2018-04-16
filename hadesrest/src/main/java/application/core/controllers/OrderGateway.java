package application.core.controllers;

import application.core.repositories.TraderRepository;
import application.core.security.TokenManager;
import application.core.services.OrderService;
import application.core.services.TraderDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import models.entities.Order;
import models.entities.Trader;
import models.messages.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@PermitAll
public class OrderGateway {

    private final ObjectWriter objectWriter;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TraderRepository traderRepository;

    public OrderGateway(){
        this.objectWriter = new ObjectMapper().writer();
        
    }

    /**
     *
     * @param order Order to enter into the order book
     * @return JSONified ApiMessage containing whether it was succesful or if not, contains a message explaining
     */
    @PostMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiMessage> createOrder(@Valid @RequestBody Order order) throws TimeoutException {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Trader t = traderRepository.findByUsername(userName);

            Long traderId = traderRepository.findByUsername(userName).getId();
            order.setTraderId(traderId);
            //ToDo: This should be part of the engine.
            if(t.getAccount().getCapital().compareTo(order.getSize().multiply(order.getPrice())) < 0){
                return ResponseEntity.ok(new ApiMessage(false, "Not enough capital"));
            }
            ApiMessage res = orderService.processOrder(objectWriter.writeValueAsString(order));

            if(res.getSuccess()){
                orderService.saveOrder(order);
            }
            else{
                return ResponseEntity.ok(new ApiMessage(false, "failed to process order"));
            }
            return ResponseEntity.ok().body(res);

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new ApiMessage(false, "failed to process order"));
        }
    }

    /**
     *
     * @return A list of open orders for the currently signed in trader.
     */
    @GetMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrdersForTrader(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Long traderId = traderRepository.findByUsername(userName).getId();
        return ResponseEntity.ok().body(orderService.getOrdersForTrader(traderId));
    }
}
