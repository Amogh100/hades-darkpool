package application.core.controllers;

import application.core.models.Order;
import application.core.models.messages.ApiMessage;
import application.core.services.OrderService;
import application.core.services.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiMessage> createOrder(@Valid @RequestBody Order order){
        ApiMessage res = OrderValidator.validate(order);
        if(res.getSuccess()){
            orderService.addOrder(order);
        }
        return ResponseEntity.ok().body(res);
    }

    @GetMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrdersForTrader(@Valid @RequestParam("traderId") long traderId){
        return ResponseEntity.ok().body(orderService.getOrdersForTrader(traderId));
    }
}
