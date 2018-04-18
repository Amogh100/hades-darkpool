package dao;

import eventprocessing.DatabaseHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Database Access Object for Traders
 */
public class TraderDao {

   /**
    Gets the capital for a trader.
    @param traderId trader id of trader to obtain the capital for.
    */
    public static BigDecimal getCapitalForTrader(long traderId) {
        String existingCapitalQuery = "SELECT capital FROM account WHERE id = ?";
        BigDecimal existingCapital = BigDecimal.ZERO;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement statement = conn.prepareStatement(existingCapitalQuery)) {
            statement.setLong(1, traderId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                existingCapital = rs.getBigDecimal("capital");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return existingCapital;
    }

    
    /**
     Updates the capital for a given trader
     @param traderId trader id of trader to update capital for 
     @param newCapital new capital value
     */
    public static void updateCapitalForTrader(long traderId, BigDecimal newCapital){
        String dbUpdate = "UPDATE account " + "SET capital = ? "
                + "WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(dbUpdate)) {

            pstmt.setBigDecimal(1, newCapital);
            pstmt.setDouble(2, traderId);
            pstmt.execute();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}