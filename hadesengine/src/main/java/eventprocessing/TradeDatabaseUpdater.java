package eventprocessing;

import models.entities.Trade;
import structures.TradeCache;

import java.sql.*;

public class TradeDatabaseUpdater implements Runnable {

    private TradeCache cache;


    public TradeDatabaseUpdater(TradeCache cache){
        this.cache = cache;
    }

    //This runnable class constantly takes the contents of the cache and updates the trade database.
    @Override
    public void run() {
        if(cache.isEmpty()){
            return;
        }
        System.out.println("In Runnable!");

        String insertion = "INSERT INTO trade(trader1Id, trader2Id, order1Id, order2Id, price, fill_size) "
                            + "VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertion,
                     Statement.RETURN_GENERATED_KEYS)){
            for(Trade t: cache.getTrades()){
                long id = 0;
                pstmt.setLong(1, t.getTrader1Id());
                pstmt.setLong(2, t.getTrader2Id());
                pstmt.setLong(3, t.getOrder1Id());
                pstmt.setLong(4, t.getOrder2Id());
                pstmt.setDouble(5, t.getPrice());
                pstmt.setDouble(6, t.getSize());
                int affectedRows = pstmt.executeUpdate();
                if(affectedRows > 0){
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getLong(1);
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            }

            this.cache.clearCache();

        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

    }
}
