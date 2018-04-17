package structures;

import dao.OrderDao;
import dao.TradeDao;
import dao.TraderDao;
import eventprocessing.DatabaseHelper;
import models.entities.Trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class TradeManager {

    private final TradeDao tradeDao;
    private final OrderDao orderDao;
    private final TraderDao traderDao;
    private TradeCache cache;

   
    public TradeManager(TradeCache cache){
        this.cache = cache;
        this.tradeDao = new TradeDao();
        this.orderDao = new OrderDao();
        this.traderDao = new TraderDao();
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
    private void updateAccountsForTrader(long traderId, long orderId, BigDecimal price, BigDecimal size){

        BigDecimal existingCapital = traderDao.getCapitalForTrader(traderId);
        BigDecimal change = BigDecimal.valueOf(-1).multiply(size).multiply(price);
        boolean isBid = orderDao.isBid(orderId);
        if(!isBid){
            change = change.multiply(BigDecimal.valueOf(-1));
        }
        BigDecimal newCapital = existingCapital.add(change);
        this.traderDao.updateCapitalForTrader(traderId, newCapital);
    }

}
