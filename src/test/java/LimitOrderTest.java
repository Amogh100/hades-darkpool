import core.LimitOrder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LimitOrderTest {

    @Test
    public void validLimitOrderConstruction(){
        LimitOrder testLimit = new LimitOrder(1, "BTC", 0.15,true,10500);
        assertEquals(testLimit.isBid(), true);
        assertEquals(testLimit.getSize(), 0.15, 0);
        assertEquals(testLimit.getTicker(), "BTC");
        assertEquals(testLimit.getTraderId(), 1);
        assertEquals(testLimit.getPrice(), 10500, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTickerOrderConstruction(){
        LimitOrder testLimit = new LimitOrder(1, null, 0.15, true, 10500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTickerOrderConstruction(){
        LimitOrder testLimit = new LimitOrder(1, "", 0.15, true, 10500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeSizeOrderConstruction(){
        LimitOrder testLimit = new LimitOrder(1, "BTC", -1, true, 10500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativePriceOrderConstruction(){
        LimitOrder testLimit = new LimitOrder(1, "BTC", 0.15, true, -100);
    }



}
