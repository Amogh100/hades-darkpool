package structures;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;

import eventprocessing.DatabaseHelper;
import models.entities.Position;

public class PositionCache {

    private static HashMap<Long, ArrayList<Position>> traderPositions = new HashMap<>();

    private PositionCache(){
        populateCache();
    }

    //Populate the position cache from the database.
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

    //Updates the position for a given trader, in a specific asset.
    /**
      @param traderId trader id of trader to update position for
      @param assetId asset to update the position for
      @param change Change in position
     */
    public static void updatePosition(long traderId, String assetId,BigDecimal change){
        ArrayList<Position> positionsForTrader = traderPositions.getOrDefault(traderId, new ArrayList<Position>());
        if(positionsForTrader.isEmpty()){
            positionsForTrader.add(new Position(assetId, change, traderId));
            return;
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

    public static void flush() {
        if(getTraderPositions().isEmpty()){
            System.out.println("NO POSITIONS");
        }
        for(ArrayList<Position> positionsForTrader: getTraderPositions().values()){

            for(Position position: positionsForTrader){
                String sql = "UPDATE position SET position_size=? WHERE asset_id=? AND trader_id=?";
                try(Connection conn = DatabaseHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)){
                    stmt.setBigDecimal(1, position.getPositionSize());
                    stmt.setString(2, position.getAssetId());
                    stmt.setLong(3, position.getTraderId());
                    stmt.executeUpdate();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}