package application.core.services;

import application.core.OrderBook.OrderBook;
import application.core.models.Order;
import application.core.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orders;

    private OrderBook orderBook;

    public OrderService(){
        this.orderBook = new OrderBook();
    }

    public void addOrder(Order o){
        orderBook.addOrder(o);
        orders.save(o);

    }

    public List<Order> getOrdersForTrader(long traderId){
        return orders.findByTraderId(traderId);
    }

}
