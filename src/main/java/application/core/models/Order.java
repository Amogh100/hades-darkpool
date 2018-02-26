package application.core.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents an Order in the Order book.
 */
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue
    private long orderId;

    private long traderId;

    private String ticker;

    private double size;

    private boolean bid;

    private OrderType type;

    private double price;


    /**
     * @param traderId Source trader making the order
     * @param ticker Ticker (string representation of asset)
     * @param size The size of the order in number of coins
     * @param bid Boolean representing if the order is a bid or ask. True if bid.
     */

     public Order(long traderId, String ticker, double size, boolean bid, OrderType type, double price, long orderId){
         this.traderId = traderId;
         this.ticker = ticker;
         this.size = size;
         this.bid = bid;
         this.type = type;
         this.price = price;
         this.orderId = orderId;
     }

     public Order(){}

     public long getTraderId(){
         return traderId;
     }

     public String getTicker(){
         return ticker;
     }

     public double getSize(){
         return size;
     }

     public boolean isBid(){
         return bid;
     }

     public OrderType getType(){return type;}

     public double getPrice(){return price;}

     public long getOrderId(){return orderId;}
}
