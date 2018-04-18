package dao;

import eventprocessing.DatabaseHelper;
import models.OrderType;
import models.entities.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDao {

    public static boolean isBid(long globalOrderId){
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

    public static HashMap<Long, ArrayList<Order>> getAllOpenOrders(){
        String openOrdersQuery = "SELECT * FROM orders WHERE filled = false";
        HashMap<Long, ArrayList<Order>> results = new HashMap<>();
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement statement = conn.prepareStatement(openOrdersQuery)) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String ticker = rs.getString("ticker");
                BigDecimal price = rs.getBigDecimal("price");
                BigDecimal size = rs.getBigDecimal("size");
                boolean bid = rs.getBoolean("bid");
                long traderId = rs.getLong("trader_id");
                long id = rs.getLong("global_order_id");
                long type = rs.getInt("type");
                OrderType orderType = OrderType.LIMIT;
                if(type == 0){
                    orderType = OrderType.MARKET;
                }
                Order order = new Order(traderId, ticker,size, bid, orderType, price, id);
                ArrayList<Order> openOrdersForTrader = results.getOrDefault(traderId, new ArrayList<>());
                openOrdersForTrader.add(order);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return results;
    }
}
