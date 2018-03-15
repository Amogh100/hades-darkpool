package application.core.controllers;

import application.core.models.Order;
import application.core.models.messages.ApiMessage;
import application.core.services.OrderService;
import application.core.services.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@PermitAll
public class OrderGateway {

    @Autowired
    private OrderService orderService;

    /**
     *
     * @param order Order to enter into the order book
     * @return JSONified ApiMessage containing whether it was succesful or if not, contains a message explaining
     */
    @PostMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiMessage> createOrder(@Valid @RequestBody Order order){
        ApiMessage res = OrderValidator.validate(order);
        if(res.getSuccess()){
            orderService.addOrder(order);
        }
        return ResponseEntity.ok().body(res);
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
