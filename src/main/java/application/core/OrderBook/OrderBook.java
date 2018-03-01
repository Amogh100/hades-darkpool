package application.core.OrderBook;

import application.core.models.Order;

import java.util.*;

public class OrderBook {

    /**
     * O(1) to get the orders associated with a price level. Use additional
     * O(N) space to store just the prices in a priority queue for both bids
     * and asks so that bestBid and bestAsk queries are also O(1)
     */
    private HashMap<Double, LinkedList<Long>> bids;
    private HashMap<Double, LinkedList<Long>> asks;

    //Want max heap for bids
    private PriorityQueue<Double> bidPrices;

    //Min heap for asks
    private PriorityQueue<Double> askPrices;

    public OrderBook(){
        this.bids = new HashMap();
        this.asks = new HashMap();
        this.bidPrices = new PriorityQueue<>(Collections.reverseOrder());
        this.askPrices = new PriorityQueue<>();
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
            bidPrices.add(price);

        }
        else{
            LinkedList<Long> existingAsksAtPriceLevel = asks.get(price);
            if(existingAsksAtPriceLevel == null){
                existingAsksAtPriceLevel = new LinkedList<>();
            }
            existingAsksAtPriceLevel.add(order.getOrderId());
            asks.put(price, existingAsksAtPriceLevel);
            askPrices.add(price);
        }
        return true;
    }

    /**
     * Returns the best bid in the order book.
     */
    public double getBestBid(){
        return bidPrices.poll();
    }

    /**
     * Returns the best ask in the order book
     */
    public double getBestAsk(){
        return askPrices.poll();
    }


}
