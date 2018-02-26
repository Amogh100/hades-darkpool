package application.core.services;

import application.core.models.Order;
import application.core.models.OrderType;
import application.core.models.messages.ApiMessage;

public class OrderValidator {

    private static boolean validSize(Order o){
        return o.getSize() > 0;
    }

    private static boolean validPrice(Order o){
        if(o.getType() == OrderType.LIMIT){
            return o.getPrice() > 0;
        }
        return true;
    }

    public static ApiMessage validate(Order o){
        if(!validSize(o)){
            return new ApiMessage(false, "Invalid Order Size");
        }
        if(!validPrice(o)){
            return new ApiMessage(false, "Invalid Price. Must be bigger than 0");
        }
        return new ApiMessage(true, "Validated");
    }


}
