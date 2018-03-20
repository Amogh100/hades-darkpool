import junit.framework.TestCase;
import models.entities.Order;
import models.OrderType;
import org.junit.Test;


public class OrderTest {

    @Test
    public void validLimitOrderConstruction(){
        Order testLimit = new Order(1, "BTC", 0.15,true, OrderType.LIMIT, 10400, 1);
        TestCase.assertEquals(testLimit.isBid(), true);
        TestCase.assertEquals(testLimit.getSize(), 0.15, 0);
        TestCase.assertEquals(testLimit.getTicker(), "BTC");
        TestCase.assertEquals(testLimit.getTraderId(), 1);
        TestCase.assertEquals(testLimit.getPrice(), 10400, 0);
        TestCase.assertEquals(testLimit.getGlobalOrderId(),1);
    }

}
