import junit.framework.TestCase;
import models.OrderType;
import models.entities.Order;
import orderbook.OrderBook;
import orderbook.OrderBookFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderBookTest {


    @Test
    public void simpleCross(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100);
        Order o1 = new Order(1,"BTC",100, true, OrderType.LIMIT,9900.99,1);
        Order o2 = new Order(2,"BTC",50, false, OrderType.LIMIT,9900.99,2);
        book.addOrder(o1);
        book.addOrder(o2);
        TestCase.assertEquals(book.getBestBid(), 9900.99);
        TestCase.assertEquals(o1.getSize(), 50.0,0);
        TestCase.assertEquals(book.priceLevelExists(9900.99), true);
        TestCase.assertEquals(book.getBestAsk(), null);
    }

    @Test
    public void partialFills(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100);
        Order o1 = new Order(1,"BTC",1000.103, true, OrderType.LIMIT,9900.99,1);
        Order o2 = new Order(2,"BTC",900.904, true, OrderType.LIMIT,9900.99,2);
        Order o3 = new Order(3,"BTC",200, true, OrderType.LIMIT,9900.99,3);
        Order o4 = new Order(4,"BTC",5, false, OrderType.LIMIT,9900.99,4);
        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        book.addOrder(o4);
        TestCase.assertEquals(o1.getSize(), 995.103);
        TestCase.assertEquals(o2.getSize(), 900.904);
        TestCase.assertEquals(o3.getSize(), 200.0);
        TestCase.assertEquals(o4.getSize(), 0.0);
        TestCase.assertEquals(book.priceLevelExists(9900.99), true);
        TestCase.assertEquals(book.getBestBid(), 9900.99);
        TestCase.assertEquals(book.getBestAsk(), null);
        Order o5= new Order(4,"BTC",50, false, OrderType.LIMIT,9901.00,4);
        book.addOrder(o5);
        TestCase.assertEquals(book.priceLevelExists(9900.99), true);
        TestCase.assertEquals(book.priceLevelExists(9901.00), true);
    }

    @Test
    public void orderFillsExactlyAllWithPriceCorrection(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100);
        Order o1 = new Order(1,"BTC",1000.103, true, OrderType.LIMIT,9900.99,1);
        Order o2 = new Order(2,"BTC",900.904, true, OrderType.LIMIT,9900.99,2);
        Order o3 = new Order(3,"BTC",200.0, true, OrderType.LIMIT,9900.99,3);
        Order o4 = new Order(4,"BTC",2101.007, false, OrderType.LIMIT,9900.95,4);
        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        TestCase.assertEquals(book.getBestAsk(), null);
        TestCase.assertEquals(book.getBestBid(), 9900.99);
        book.addOrder(o4);
        TestCase.assertEquals(false, book.priceLevelExists(9900.99));
        TestCase.assertEquals(0.0, o1.getSize());
        TestCase.assertEquals(0.0, o2.getSize());
        TestCase.assertEquals(0.0, o3.getSize());
        TestCase.assertEquals(0.0, o4.getSize());
    }

    @Test
    public void noFills(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100);
        Order o1 = new Order(1,"BTC",1000.103, true, OrderType.LIMIT,9900.90,1);
        Order o2 = new Order(2,"BTC",900.904, true, OrderType.LIMIT,9900.91,2);
        Order o3 = new Order(3,"BTC",200.0, true, OrderType.LIMIT,9900.92,3);
        Order o4 = new Order(4,"BTC",2101.007, true, OrderType.LIMIT,9900.93,4);
        Order o5 = new Order(1,"BTC",100.0, false, OrderType.LIMIT,9900.95,1);
        Order o6 = new Order(2,"BTC",200.0, false, OrderType.LIMIT,9900.96,2);
        Order o7 = new Order(3,"BTC",300.0, false, OrderType.LIMIT,9900.97,3);
        Order o8 = new Order(4,"BTC",400.0, false, OrderType.LIMIT,9900.98,4);

        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        book.addOrder(o4);
        book.addOrder(o5);
        book.addOrder(o6);
        book.addOrder(o7);
        book.addOrder(o8);

        TestCase.assertEquals(book.getBestAsk(), 9900.95);
        TestCase.assertEquals(book.getBestBid(), 9900.93);



    }


}
