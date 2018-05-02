import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import structures.PositionCache;
import models.entities.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Before this test is run make sure that the position table 
 * is loaded in Postgres
 */
@Ignore
public class PositionCacheTest {

    @Before
    public void setUp(){
        PositionCache.populateCache();
    }

    @Test
    public void populateTest(){
        HashMap<Long, ArrayList<Position>> positions = PositionCache.getTraderPositions();
        ArrayList<Position> trader1Positions = positions.get(Long.valueOf(1));
        for(Position pos: trader1Positions){
            if(pos.getAssetId().equals("BTC")){
                assertEquals(pos.getPositionSize(), BigDecimal.valueOf(2.0));
            }
            if(pos.getAssetId().equals("XRP")){
                assertEquals(pos.getPositionSize(), BigDecimal.valueOf(300.8));
            }
        }
        ArrayList<Position> trader2Positions = positions.get(Long.valueOf(2));
        for(Position pos: trader2Positions){
            if(pos.getAssetId().equals("BTC")){
                assertEquals(pos.getPositionSize(), BigDecimal.valueOf(2.0));
            }
            if(pos.getAssetId().equals("LTC")){
                assertEquals(pos.getPositionSize(), BigDecimal.valueOf(5.9));
            }
        }
        ArrayList<Position> trader3Positions = positions.get(Long.valueOf(3));
        for(Position pos: trader3Positions){
            assertEquals(pos.getPositionSize(), BigDecimal.valueOf(3.0));
        }
    }
}