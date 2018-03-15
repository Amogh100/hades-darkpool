import application.core.OrderBook.OrderBook;
import application.core.OrderBook.OrderBookFactory;
import application.core.OrderBook.OrderQueue;
import application.core.models.Order;
import application.core.models.OrderType;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.BlockingQueue;

public class OrderBookTests {

    private static OrderBook book;

    @BeforeClass
    public static void setup(){
        BlockingQueue<Order> queue = OrderQueue.getQueueInstance();
        book = OrderBookFactory.createNewOrderbook("BTC", 100, queue);
    }

    @Test
    public void testBestBid(){
        Order o1 = new Order(1, "BTC", 100, true, OrderType.LIMIT, 9800.99, 1);
        Order o2 = new Order(1, "BTC", 200, true, OrderType.LIMIT, 9900.23, 2);
        Order o3 = new Order(1, "BTC", 14, true, OrderType.LIMIT, 9700.45, 3);

        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);

        assertEquals(book.getBestBid(), 9900.23, 0);
    }


    @Test
    public void testBestAsk(){
        Order o1 = new Order(1, "BTC", 100, false, OrderType.LIMIT, 9800.99, 1);
        Order o2 = new Order(1, "BTC", 200, false, OrderType.LIMIT, 9900.23, 2);
        Order o3 = new Order(1, "BTC", 14, false, OrderType.LIMIT, 9700.45, 3);

        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);

        assertEquals(book.getBestAsk(), 9700.45, 0);
    }
}
