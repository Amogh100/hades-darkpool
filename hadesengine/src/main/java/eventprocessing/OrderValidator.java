package eventprocessing;

import dao.TraderDao;
import models.entities.Order;
import models.OrderType;
import models.messages.ApiMessage;
import structures.OpenOrderCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OrderValidator {

    /**
     *
     * @param order Order to check if it has a valid size
     * @return true if the order has a valid size, false otherwise
     */
    private static boolean validSize(Order order){
        return order.getSize().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     *
     * @param order to check if it has a valid price
     * @return true if order has valid price, false otherwise
     */
    private static boolean validPrice(Order order){
        if(order.getType() == OrderType.LIMIT){
            return order.getPrice().compareTo(BigDecimal.ZERO) > 0;
        }
        return true;
    }

    /**
     *
     * @param order Order to check if it is valid
     * @return APIMessage containing if the order is valid and if not a message why order is invalid
     */
    public static ApiMessage validate(Order order, Set<String> validTickers){
        if(!validExposure(order)){
            return new ApiMessage(false, "Invalid Order Exposure!");
        }
        if(!validSize(order)){
            return new ApiMessage(false, "Invalid Order Size");
        }
        if(!validPrice(order)){
            return new ApiMessage(false, "Invalid Price. Must be bigger than 0");
        }
        if(!validTickers.contains(order.getTicker())){
            return new ApiMessage(false, "Invalid ticker. Only supports BTC, XRP, LTC, and ETH");
        }
        return new ApiMessage(true, "Validated");
    }

    private static boolean validExposure(Order order) {
        BigDecimal orderValue = order.getPrice().multiply(order.getSize());
        BigDecimal totalExposure = BigDecimal.ZERO;
        ConcurrentHashMap<Long, ArrayList<Order>> openOrders = OpenOrderCache.getOpenOrders();
        ArrayList<Order> orders = openOrders.get(order.getTraderId());
        if(orders == null){
            return true;
        }
        for(Order existingOpenOrder: orders){
            if(existingOpenOrder.getTicker().equals(order.getTicker())){
                totalExposure = totalExposure.add(existingOpenOrder.getPrice().multiply(existingOpenOrder.getSize()));
            }
        }
        BigDecimal traderCapital = TraderDao.getCapitalForTrader(order.getTraderId());
        return traderCapital.subtract(totalExposure).compareTo(orderValue) > 0;
    }


}
