import junit.framework.TestCase;
import models.OrderType;
import models.entities.Order;
import orderbook.OrderBook;
import orderbook.OrderBookFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderBookTest {


    @Test
    public void simpleCross(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100, false);
        Order o1 = new Order(1,"BTC", BigDecimal.valueOf(100), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(50), false, OrderType.LIMIT,BigDecimal.valueOf(9900.99),2);
        book.addOrder(o1);
        book.addOrder(o2);
        TestCase.assertEquals(BigDecimal.valueOf(9900.99),book.getBestBid());
        TestCase.assertEquals(o1.getSize(), BigDecimal.valueOf(50));
        TestCase.assertEquals(book.priceLevelExists(BigDecimal.valueOf(9900.99)), true);
        TestCase.assertEquals(book.getBestAsk(), null);
    }

    @Test
    public void partialFills(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100, false);
        Order o1 = new Order(1,"BTC",BigDecimal.valueOf(1000.103), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(900.904), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),2);
        Order o3 = new Order(3,"BTC",BigDecimal.valueOf(200), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),3);
        Order o4 = new Order(4,"BTC",BigDecimal.valueOf(5), false, OrderType.LIMIT,BigDecimal.valueOf(9900.99),4);
        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        book.addOrder(o4);
        TestCase.assertEquals(BigDecimal.valueOf(995.103), o1.getSize());
        TestCase.assertEquals(BigDecimal.valueOf(900.904), o2.getSize());
        TestCase.assertEquals(BigDecimal.valueOf(200), o3.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o4.getSize());
        TestCase.assertEquals(book.priceLevelExists(BigDecimal.valueOf(9900.99)), true);
        TestCase.assertEquals(BigDecimal.valueOf(9900.99),book.getBestBid());
        TestCase.assertEquals(book.getBestAsk(), null);
        Order o5= new Order(4,"BTC",BigDecimal.valueOf(50), false, OrderType.LIMIT,BigDecimal.valueOf(9901.00),4);
        book.addOrder(o5);
        TestCase.assertEquals(book.priceLevelExists(BigDecimal.valueOf(9900.99)), true);
        TestCase.assertEquals(book.priceLevelExists(BigDecimal.valueOf(9901.00)), true);
    }

    @Test
    public void orderFillsExactlyAllWithPriceCorrection(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100, false);
        Order o1 = new Order(1,"BTC",BigDecimal.valueOf(1000.103), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(900.904), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),2);
        Order o3 = new Order(3,"BTC",BigDecimal.valueOf(200.0), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),3);
        Order o4 = new Order(4,"BTC",BigDecimal.valueOf(2101.007), false, OrderType.LIMIT,BigDecimal.valueOf(9900.95),4);
        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        TestCase.assertEquals(book.getBestAsk(), null);
        TestCase.assertEquals(BigDecimal.valueOf(9900.99), book.getBestBid());
        book.addOrder(o4);
        TestCase.assertEquals(book.priceLevelExists(BigDecimal.valueOf(9900.99)), false);
        TestCase.assertEquals(BigDecimal.ZERO, o1.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o2.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o3.getSize());
    }

    @Test
    public void noFills(){
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100, false);
        Order o1 = new Order(1,"BTC",BigDecimal.valueOf(1000.103), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(900.904), true, OrderType.LIMIT, BigDecimal.valueOf(9900.91),2);
        Order o3 = new Order(3,"BTC",BigDecimal.valueOf(200.0), true, OrderType.LIMIT,BigDecimal.valueOf(9900.92),3);
        Order o4 = new Order(4,"BTC",BigDecimal.valueOf(2101.007), true, OrderType.LIMIT,BigDecimal.valueOf(9900.93),4);
        Order o5 = new Order(1,"BTC",BigDecimal.valueOf(100.0), false, OrderType.LIMIT,BigDecimal.valueOf(9900.95),1);
        Order o6 = new Order(2,"BTC",BigDecimal.valueOf(200.0), false, OrderType.LIMIT,BigDecimal.valueOf(9900.96),2);
        Order o7 = new Order(3,"BTC",BigDecimal.valueOf(300.0), false, OrderType.LIMIT,BigDecimal.valueOf(9900.97),3);
        Order o8 = new Order(4,"BTC",BigDecimal.valueOf(400.0), false, OrderType.LIMIT,BigDecimal.valueOf(9900.98),4);

        book.addOrder(o1);
        book.addOrder(o2);
        book.addOrder(o3);
        book.addOrder(o4);
        book.addOrder(o5);
        book.addOrder(o6);
        book.addOrder(o7);
        book.addOrder(o8);

        TestCase.assertEquals(BigDecimal.valueOf(9900.95), book.getBestAsk());
        TestCase.assertEquals(BigDecimal.valueOf(9900.93), book.getBestBid());

    }

    @Test
    public void manyBuysOneBigSell(){
        System.out.println("RUNNING BUYS ONE BIG SELL\n\n\n");
        OrderBook book = OrderBookFactory.createNewOrderbook("BTC", 100, false);
        Order o1 = new Order(1,"BTC",BigDecimal.valueOf(4000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(1000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),2);
        Order o3 = new Order(3,"BTC",BigDecimal.valueOf(1000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),3);
        Order o4 = new Order(4,"BTC",BigDecimal.valueOf(1000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),4);
        Order o5 = new Order(1,"BTC",BigDecimal.valueOf(1000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),5);
        Order o6 = new Order(2,"BTC",BigDecimal.valueOf(1000), true, OrderType.LIMIT,BigDecimal.valueOf(9900.90),6);
        List<Order> orders = Arrays.asList(o1, o2, o3, o4, o5,o6);
        for(Order o: orders){
            book.addOrder(o);
        }
        Order o7 = new Order(2,"BTC",BigDecimal.valueOf(7500), false, OrderType.LIMIT,BigDecimal.valueOf(9900.90),7);
        book.addOrder(o7);

        TestCase.assertEquals(BigDecimal.ZERO, o7.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o1.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o2.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o3.getSize());
        TestCase.assertEquals(BigDecimal.ZERO, o4.getSize());
        TestCase.assertEquals(BigDecimal.valueOf(500),o5.getSize());
        TestCase.assertEquals(BigDecimal.valueOf(1000),o6.getSize());


    }


}
