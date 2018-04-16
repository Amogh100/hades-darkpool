import eventprocessing.OrderValidator;
import junit.framework.TestCase;
import models.OrderType;
import models.entities.Order;
import models.messages.ApiMessage;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

public class OrderValidationTest {

    private static HashSet<String> tickers;

    @BeforeClass
    public static void setUp(){
        tickers = new HashSet<String>(Arrays.asList("BTC", "XRP", "LTC", "ETH"));
    }


    @Test
    public void nonPositiveSizeIsInvalid(){
        //For both limit and market orders, all order sizes should be greater than 0

        Order o1 = new Order(1,"BTC", BigDecimal.ZERO, true, OrderType.MARKET, BigDecimal.valueOf(4.3),1);
        ApiMessage response = OrderValidator.validate(o1, tickers);
        TestCase.assertEquals(response.getSuccess(), false);
        TestCase.assertEquals(response.getMessage(), "Invalid Order Size");


        o1.setType(OrderType.LIMIT);
        response = OrderValidator.validate(o1, tickers);
        TestCase.assertEquals(response.getSuccess(), false);
        TestCase.assertEquals(response.getMessage(), "Invalid Order Size");

    }

    @Test
    public void nonPositivePriceForLimitIsInvalid(){
        Order o1 = new Order(1,"BTC", BigDecimal.valueOf(14), true, OrderType.LIMIT, BigDecimal.ZERO,1);
        ApiMessage response = OrderValidator.validate(o1, tickers);
        TestCase.assertEquals(response.getSuccess(), false);
        TestCase.assertEquals(response.getMessage(), "Invalid Price. Must be bigger than 0");
    }

    @Test
    public void zeroPriceMarketOrdersAreValid(){
        Order o1 = new Order(1, "BTC", BigDecimal.valueOf(100), true, OrderType.MARKET, BigDecimal.ZERO, 1);
        ApiMessage response = OrderValidator.validate(o1, tickers);
        TestCase.assertEquals(response.getSuccess(), true);
    }

    @Test
    public void invalidTickers(){
        Order o1 = new Order(1, "BLAH", BigDecimal.valueOf(100), true, OrderType.LIMIT, BigDecimal.valueOf(10),1);
        ApiMessage res = OrderValidator.validate(o1, tickers);
        TestCase.assertEquals(res.getSuccess(), false);
    }

    @Test
    public void validOrders(){
        Order o1 = new Order(1, "ETH", BigDecimal.valueOf(100), false, OrderType.LIMIT, BigDecimal.valueOf(400.0), 1);
        Order o2 = new Order(1, "BTC", BigDecimal.valueOf(53), true, OrderType.LIMIT, BigDecimal.valueOf(9800), 2);
        Order o3 = new Order(1,"XRP", BigDecimal.valueOf(2000), false, OrderType.MARKET, BigDecimal.valueOf(0.99),3);

        ApiMessage r1 = OrderValidator.validate(o1, tickers);
        ApiMessage r2 = OrderValidator.validate(o2, tickers);
        ApiMessage r3 = OrderValidator.validate(o3, tickers);

        TestCase.assertEquals(r1.getSuccess(), true);
        TestCase.assertEquals(r2.getSuccess(), true);
        TestCase.assertEquals(r3.getSuccess(), true);


    }

}
