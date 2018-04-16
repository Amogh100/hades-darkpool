import junit.framework.TestCase;
import models.entities.Order;
import models.OrderType;
import org.junit.Test;

import java.math.BigDecimal;


public class OrderTest {

    @Test
    public void validLimitOrderConstruction(){
        Order testLimit = new Order(1, "BTC", BigDecimal.valueOf(0.15),true, OrderType.LIMIT, BigDecimal.valueOf(10400), 1);
        TestCase.assertEquals(testLimit.isBid(), true);
        TestCase.assertEquals(testLimit.getSize(), BigDecimal.valueOf(0.15));
        TestCase.assertEquals(testLimit.getTicker(), "BTC");
        TestCase.assertEquals(testLimit.getTraderId(), 1);
        TestCase.assertEquals(BigDecimal.valueOf(10400),testLimit.getPrice());
        TestCase.assertEquals(testLimit.getGlobalOrderId(),1);
    }

}
