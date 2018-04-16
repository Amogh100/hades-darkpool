package structures;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import eventprocessing.DatabaseHelper;
import models.Position;

public class PositionCache {

    private static HashMap<Long, ArrayList<Position>> traderPositions = new HashMap<>();

    private PositionCache(){
        populateCache();
    }

    public static void populateCache(){
        String positionQuery = "SELECT * from position";
        try{
            Connection conn = DatabaseHelper.connect();
            Statement st = conn.createStatement();
            ResultSet results = st.executeQuery(positionQuery);
            while(results.next()){
                long traderId = results.getLong("trader_id");
                ArrayList<Position> positions = traderPositions.get(traderId);
                if(positions == null){
                    positions = new ArrayList<Position>();
                    traderPositions.put(traderId, positions);
                }
                String asset = results.getString("asset_id");
                BigDecimal position = results.getBigDecimal("position_size");
                positions.add(new Position(asset, position, traderId));
            }
            

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static HashMap<Long, ArrayList<Position>> getTraderPositions(){
        return traderPositions;
    }

    public static void updatePosition(long traderId, String assetId,BigDecimal change){
        ArrayList<Position> positionsForTrader = traderPositions.get(traderId);
        if(positionsForTrader == null){
            throw new NullPointerException("Failed to find positions for trader " + traderId);
        }
        for(Position pos: positionsForTrader){
            if(pos.getAssetId().equals(assetId)){
                pos.updatePositionSize(change);
                return;
            }
        }
        //Add a new position
        positionsForTrader.add(new Position(assetId, change, traderId));

    }
}