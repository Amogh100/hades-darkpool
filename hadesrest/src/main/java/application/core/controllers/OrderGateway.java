package application.core.controllers;

import application.core.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import models.entities.Order;
import models.messages.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            ApiMessage res = orderService.processOrder(objectWriter.writeValueAsString(order));
            if(res.getSuccess()){
                orderService.saveOrder(order);
            }
            return ResponseEntity.ok().body(res);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(new ApiMessage(false, "failed to process order"));

    }

    /**
     *
     * @param traderId TraderId of trader to get the open orders for
     * @return A list of open orders for the trader with id traderId
     */
    @GetMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrdersForTrader(@Valid @RequestParam("traderId") long traderId){
        return ResponseEntity.ok().body(orderService.getOrdersForTrader(traderId));
    }
}
