import application.core.models.Order;
import application.core.models.OrderType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void validLimitOrderConstruction(){
        Order testLimit = new Order(1, "BTC", 0.15,true, OrderType.LIMIT, 10400, 1);
        assertEquals(testLimit.isBid(), true);
        assertEquals(testLimit.getSize(), 0.15, 0);
        assertEquals(testLimit.getTicker(), "BTC");
        assertEquals(testLimit.getTraderId(), 1);
        assertEquals(testLimit.getPrice(), 10400, 0);
        assertEquals(testLimit.getOrderId(),1);
    }

}
