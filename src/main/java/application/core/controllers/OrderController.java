package application.core.controllers;

import application.core.models.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class OrderController {

    @PostMapping(value="/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order){
        return ResponseEntity.ok().body(order);
    }

}
