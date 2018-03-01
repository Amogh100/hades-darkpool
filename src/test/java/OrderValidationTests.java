import application.core.models.Order;
import application.core.models.OrderType;
import application.core.models.messages.ApiMessage;
import application.core.services.OrderValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderValidationTests {

    @Test
    public void nonPositiveSizeIsInvalid(){
        //For both limit and market orders, all order sizes should be greater than 0
        Order o1 = new Order(1,"BTC", 0, true, OrderType.MARKET, 4.3,1);
        ApiMessage response = OrderValidator.validate(o1);
        assertEquals(response.getSuccess(), false);
        assertEquals(response.getMessage(), "Invalid Order Size");


        o1.setType(OrderType.LIMIT);
        response = OrderValidator.validate(o1);
        assertEquals(response.getSuccess(), false);
        assertEquals(response.getMessage(), "Invalid Order Size");

    }

    @Test
    public void nonPositivePriceForLimitIsInvalid(){
        Order o1 = new Order(1,"BTC", 14, true, OrderType.LIMIT, 0.0,1);
        ApiMessage response = OrderValidator.validate(o1);
        assertEquals(response.getSuccess(), false);
        assertEquals(response.getMessage(), "Invalid Price. Must be bigger than 0");
    }

    @Test
    public void zeroPriceMarketOrdersAreValid(){
        Order o1 = new Order(1, "BTC", 100, true, OrderType.MARKET, 0.0, 1);
        ApiMessage response = OrderValidator.validate(o1);
        assertEquals(response.getSuccess(), true);
    }

    @Test
    public void validOrders(){
        Order o1 = new Order(1, "ETH", 100, false, OrderType.LIMIT, 400.0, 1);
        Order o2 = new Order(1, "BTC", 53, true, OrderType.LIMIT, 9800, 2);
        Order o3 = new Order(1,"XRP", 2000, false, OrderType.MARKET, 0.99,3);

        ApiMessage r1 = OrderValidator.validate(o1);
        ApiMessage r2 = OrderValidator.validate(o2);
        ApiMessage r3 = OrderValidator.validate(o3);

        assertEquals(r1.getSuccess(), true);
        assertEquals(r2.getSuccess(), true);
        assertEquals(r3.getSuccess(), true);


    }

}
