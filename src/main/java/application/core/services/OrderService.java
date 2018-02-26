package application.core.services;

import application.core.models.Order;
import application.core.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orders;

    public void addOrder(Order o){
        orders.save(o);
    }

    public List<Order> getOrdersForTrader(long traderId){
        return orders.findByTraderId(traderId);
    }

}
