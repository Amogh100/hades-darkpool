package application.core.services;

import application.core.OrderBook.OrderBook;
import application.core.models.Order;
import application.core.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service that contains main business logic
 * for adding orders and querying for relevant orders.
 * Abstracts away access to the database
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orders;

    private OrderBook orderBook;

    public OrderService(){
        this.orderBook = new OrderBook();
    }

    /**
     *
     * @param order Order to add to orderbook and store in database
     */
    public void addOrder(Order order){
        orderBook.addOrder(order);
        orders.save(order);

    }

    /**
     *
     * @param traderId id of the trader to get the open orders of
     * @return List of open orders entered by the trader with id traderId.
     */
    public List<Order> getOrdersForTrader(long traderId){
        return orders.findByTraderId(traderId);
    }

}
