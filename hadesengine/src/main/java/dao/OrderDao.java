package dao;

import eventprocessing.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDao {
    public boolean isBid(long globalOrderId){
        String isBidQuery = "SELECT bid FROM orders WHERE global_order_id = ?";
        boolean isBid = true;
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement statement = conn.prepareStatement(isBidQuery)) {
            statement.setLong(1, globalOrderId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                isBid = rs.getBoolean("bid");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return isBid;
    }
}
