package application.core.OrderBook;

import application.core.models.Order;

import java.util.LinkedList;
import java.util.TreeMap;

public class OrderBook {

    //Maintain a tree map, mapping from price levels to the order ids at that price level.
    //This will allow for best bid/best ask queries for O(1). Although inserts will be O(log n).
    private TreeMap<Double, LinkedList<Long>> bids;
    private TreeMap<Double, LinkedList<Long>> asks;

    public OrderBook(){
        this.bids = new TreeMap<>();
        this.asks = new TreeMap<>();
    }

    /**
     * Simply add order to bids and asks depending on order type.
     *
     * @Param Order order to add to order book.
     * ToDo: Actual matching algorithm, and order book for each asset.
     * ToDo: Updgrade market orders so that their price is updated to either the best bid or ask.
     *
     */

    public boolean addOrder(Order order){
        double price = order.getPrice();
        if(order.isBid()){
            LinkedList<Long> existingBidsAtPriceLevel = bids.get(price);
            if(existingBidsAtPriceLevel == null){
                existingBidsAtPriceLevel = new LinkedList<>();
            }
            existingBidsAtPriceLevel.add(order.getOrderId());
            bids.put(price, existingBidsAtPriceLevel);
        }
        else{
            LinkedList<Long> existingAsksAtPriceLevel = asks.get(price);
            if(existingAsksAtPriceLevel == null){
                existingAsksAtPriceLevel = new LinkedList<>();
            }
            existingAsksAtPriceLevel.add(order.getOrderId());
            asks.put(price, existingAsksAtPriceLevel);
        }
        return true;
    }

    /**
     * Returns the best bid in the order book.
     */
    public double getBestBid(){
        return bids.lastKey();
    }

    /**
     * Returns the best ask in the order book
     */
    public double getBestAsk(){
        return asks.firstKey();
    }


}
