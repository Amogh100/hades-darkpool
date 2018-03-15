package application.core.services;

import application.core.OrderBook.OrderBook;
import application.core.OrderBook.OrderBookFactory;
import application.core.OrderBook.OrderQueue;
import application.core.models.Order;
import application.core.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Service that contains main business logic
 * for adding orders and querying for relevant orders.
 * Abstracts away access to the database
 */
@Service
public class OrderService {

    private final BlockingQueue<Order> orderQueue;

    @Autowired
    private OrderRepository orders;

    private HashSet<String> tickers;

    private HashMap<String, OrderBook> orderBooks;

    public OrderService(){
        this.tickers = new HashSet<String>();
        //ToDo query these from the database
        tickers.add("BTC");
        tickers.add("ETH");
        tickers.add("XRP");
        tickers.add("LTC");
        this.orderQueue = OrderQueue.getQueueInstance();
        this.orderBooks = new HashMap();
        for(String t: tickers){
            OrderBook book = OrderBookFactory.createNewOrderbook(t, 20, orderQueue);
            orderBooks.put(t, book);
        }
    }

    /**
     *
     * @param order Order to add to orderbook and store in database
     */
    public void addOrder(Order order){
        OrderBook b = orderBooks.get(order.getTicker());
        if(b == null){
            return;
        }
        else {
            orderQueue.add(order);
        }
    }

    /**
     *
     * @param traderId id of the trader to get the open orders of
     * @return List of open orders entered by the trader with id traderId.
     */
    public List<Order> getOrdersForTrader(long traderId){
        return orders.findByTraderId(traderId);
    }

    public void processOrders(){
        while(true){
            synchronized (this){
                if(orderQueue.isEmpty()){

                }
                else{

                }
            }
        }
    }

}
