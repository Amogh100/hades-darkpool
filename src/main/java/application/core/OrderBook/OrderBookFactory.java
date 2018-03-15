package application.core.OrderBook;

import java.util.concurrent.BlockingQueue;
import application.core.models.Order;


public class OrderBookFactory {

    /**
     * Creates a new Order Book from the given parameters
     * @param ticker Instrument for the order book
     * @param maxDepth The max depth for the given order book
     * @param queue The OrderQueue that is the source of the datOa for the order book.
     * @return
     */
    public static OrderBook createNewOrderbook(String ticker, int maxDepth, BlockingQueue<Order> queue){
        return new OrderBook(ticker, maxDepth, queue);
    }

}
