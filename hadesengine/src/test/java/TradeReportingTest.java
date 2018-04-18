import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import models.entities.Order;
import orderbook.OrderBook;
import orderbook.OrderBookFactory;
import models.OrderType;
import models.entities.Trade;

import java.math.BigDecimal;
import java.util.HashSet;

public class TradeReportingTest {

    OrderBook btcOrderBook;

    @Before
    public void setUp(){
        btcOrderBook = OrderBookFactory.createNewOrderbook("BTC", 100, false);
    }

    @Test
    public void simpleTrade(){
        Order o1 = new Order(1,"BTC", BigDecimal.valueOf(100), true, OrderType.LIMIT,BigDecimal.valueOf(9900.99),1);
        Order o2 = new Order(2,"BTC",BigDecimal.valueOf(50), false, OrderType.LIMIT,BigDecimal.valueOf(9900.99),2);
        btcOrderBook.addOrder(o1);
        btcOrderBook.addOrder(o2);
        HashSet<Trade> currTrades = btcOrderBook.getCurrentTrades();
        assert(currTrades != null);
        assert(currTrades.size() == 1);
        Trade expected = new Trade(1, 2,1,2, BigDecimal.valueOf(9900.99), BigDecimal.valueOf(50), "BTC");
        Trade[] trades = currTrades.toArray(new Trade[currTrades.size()]);
        Trade t = trades[0];
        TestCase.assertTrue(t.equals(expected));
    }
}