package structures;

import eventprocessing.DatabaseHelper;
import models.entities.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class TradeManager {

    private TradeCache cache;

   
    public TradeManager(TradeCache cache){
        this.cache = cache
    }
    
    //manageTrades caches all the trades and updates accounts for trade.
    //@param trades Trades to manage.
    public void manageTrades(Set<Trade> trades) throws SQLException {
        for(Trade t: trades){
            cache.addTrade(t);
            updateAccountsForTrader(t.getTrader1Id(), t.getOrder1Id(), t.getPrice(), t.getSize());
            updateAccountsForTrader(t.getTrader2Id(), t.getOrder2Id(), t.getPrice(), t.getSize());
        }
    }

    /**
      * updateAccountsForTrader updates the account status after a given trade.
      * @param traderId trader to update account status for
      * @param orderId trader's order
      * @param price price of trade
      * @param size size of trade
      */
    private void updateAccountsForTrader(long traderId, long orderId, double price, double size){
        String existingCapitalQuery = "SELECT capital FROM account WHERE id = ?";
        double existingCapital = 0;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement statement = conn.prepareStatement(existingCapitalQuery)) {
            statement.setLong(1, traderId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                existingCapital = rs.getDouble("capital");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        String isBidQuery = "SELECT bid FROM orders WHERE global_order_id = ?";
        boolean isBid = true;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement statement = conn.prepareStatement(isBidQuery)) {
            statement.setLong(1, traderId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                isBid = rs.getBoolean("bid");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        double change = -size * price;
        if(!isBid){
            change *= -1;
        }
        double newCapital = existingCapital + change;

        String dbUpdate = "UPDATE account " + "SET capital = ? "
                          + "WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(dbUpdate)) {

            pstmt.setDouble(1, newCapital);
            pstmt.setDouble(2, traderId);
            pstmt.execute();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //ToDo: failed to properly implement this for checkpoint
//    private void updatePositionsForTrader(Trader t, Order o) {
//        Set<Position> positions = t.getPositions();
//        double positionChange = o.getSize();
//        if(!o.isBid()){
//            positionChange *= -1;
//        }
//        for(Position p: positions){
//            if(p.getAssetId().equals(o.getTicker())){
//                p.updatePositionSize(positionChange);
//                String dbUpdate = "UPDATE position  " + "SET position_size = ? "
//                                  +"FROM trader " + "WHERE ? = ?";
//                try (Connection conn = DatabaseHelper.connect();
//                     PreparedStatement pstmt = conn.prepareStatement(dbUpdate)) {
//
//                    pstmt.setDouble(1, p.getPositionSize());
//                    pstmt.setLong(2, p.getTraderId());
//                    pstmt.setLong(3, t.getId());
//
//                } catch (SQLException ex) {
//                    System.out.println(ex.getMessage());
//                }
//                return;
//            }
//        }
//        positions.add(new Position(o.getTicker(), o.getSize()));
//    }

}
