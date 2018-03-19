package eventprocessing;

import models.entities.Order;
import models.OrderType;
import models.messages.ApiMessage;

import java.util.Set;

public class OrderValidator {

    /**
     *
     * @param order Order to check if it has a valid size
     * @return true if the order has a valid size, false otherwise
     */
    private static boolean validSize(Order order){
        return order.getSize() > 0;
    }

    /**
     *
     * @param order to check if it has a valid price
     * @return true if order has valid price, false otherwise
     */
    private static boolean validPrice(Order order){
        if(order.getType() == OrderType.LIMIT){
            return order.getPrice() > 0;
        }
        return true;
    }

    /**
     *
     * @param order Order to check if it is valid
     * @return APIMessage containing if the order is valid and if not a message why order is invalid
     */
    public static ApiMessage validate(Order order, Set<String> validTickers){
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


}
