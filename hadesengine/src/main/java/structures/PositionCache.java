package structures;

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
                double position = results.getDouble("position_size");
                positions.add(new Position(asset, position, traderId));
            }
            

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static HashMap<Long, ArrayList<Position>> getTraderPositions(){
        return traderPositions;
    }
}