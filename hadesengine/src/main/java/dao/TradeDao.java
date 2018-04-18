package dao;

import eventprocessing.DatabaseHelper;
import models.entities.Trade;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 Data Access Object for Trades
 */
public class TradeDao {

   //Inserts a Trade in to the database.
   /**
    @param t Trade to insert into the database.
    */
    public static void insertTrade(Trade t) {
        try {
            Connection conn = DatabaseHelper.connect();
            StringBuffer traderInsertionStatementStr = new StringBuffer();
            traderInsertionStatementStr.append("INSERT INTO trade (trader1Id, trader2Id, order1Id, order2Id, price, fillSize)");
            traderInsertionStatementStr.append(" VALUES(?, ?, ?, ?, ?, ?)");
            PreparedStatement insertStatement = conn.prepareStatement(traderInsertionStatementStr.toString());
            insertStatement.setLong(1, t.getTrader1Id());
            insertStatement.setLong(2, t.getTrader2Id());
            insertStatement.setLong(3, t.getOrder1Id());
            insertStatement.setLong(4, t.getOrder2Id());
            insertStatement.setBigDecimal(5, t.getPrice());
            insertStatement.setBigDecimal(6, t.getSize());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}