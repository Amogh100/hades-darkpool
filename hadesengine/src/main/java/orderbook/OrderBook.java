package orderbook;

import models.entities.Order;

import java.util.*;

public class OrderBook {

    private final String ticker;
    private final long maxDepth;
    private static long ORDER_ID_COUNT;

    HashMap < Double, ArrayList < Order >> priceLevels;

    //Want max heap for bids
    private PriorityQueue < Double > bidPrices;

    //Min heap for asks
    private PriorityQueue < Double > askPrices;


    public OrderBook(String ticker, long maxDepth) {
        this.ticker = ticker;
        this.maxDepth = maxDepth;
        this.bidPrices = new PriorityQueue < > (Collections.reverseOrder());
        this.askPrices = new PriorityQueue < > ();
        this.priceLevels = new HashMap < > ();
        ORDER_ID_COUNT = 0;
    }

    /**
     * Simply add order to bids and asks depending on order type.
     *
     * @Param Order order to add to order book.
     */

    public boolean addOrder(Order order) {
        switch (order.getType()) {
            case LIMIT:
                handleLimitOrder(order);
                break;
            case MARKET:
                handleMarketOrder(order);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Takes a market order, updates it to a limit order
     * where the price is the best bid/ask.
     * @PreCondition order is a market order and asks/bids exist
     * @param order Market order
     */
    private void handleMarketOrder(Order order) {
        if (order.isBid()) {
            order.setPrice(getBestAsk());
        } else if (!order.isBid()){
            order.setPrice(getBestBid());
        }
        handleLimitOrder(order);
    }

    /**
     *
     * @param order Limit Order
     */
    private void handleLimitOrder(Order order) {
        double orderPrice = order.getPrice();
        double orderSize = order.getSize();
        boolean isBid = order.isBid();
        Double currPriceLevel;
        if (isBid) {
            currPriceLevel = getBestAsk();
        } else {
            currPriceLevel = getBestBid();
        }
        order.setOrderbookId(ORDER_ID_COUNT++);
        processAtPriceLevel(order, currPriceLevel,orderPrice, orderSize);
    }

    /**
     *
     * @param order Adds order to a price level.
     * @param price Price level to add order.
     */
    private void addOrderAtPriceLevel(Order order, Double price) {
        ArrayList < Order > newOrders = priceLevels.get(price);
        if(newOrders == null){
            newOrders = new ArrayList<>();
        }
        newOrders.add(order);
        priceLevels.put(price, newOrders);
        if (order.isBid()) {
            bidPrices.add(price);
        } else {
            askPrices.add(price);
        }
    }

    /**
     *Processes and order at a given price level
     * @param order Order to price
     * @param currPriceLevel priceLevel to process order at
     * @param orderPrice price of the order
     * @param orderSize size of the order
     */
    private void processAtPriceLevel(Order order, Double currPriceLevel, Double orderPrice, Double orderSize) {
        ArrayList<Order> ordersToRemove = new ArrayList<>();
        if (currPriceLevel != null && validLimitCross(orderPrice, currPriceLevel, order.isBid())) {
            ArrayList < Order > ordersAtBestAskBid = priceLevels.get(currPriceLevel);
            while (ordersAtBestAskBid != null && validLimitCross(orderPrice, currPriceLevel, order.isBid())) {
                //ToDo: Instead of printing snapshot, generate TradeReports.
                for (int i = 0; i < ordersAtBestAskBid.size(); i++) {
                    Order currOrder = ordersAtBestAskBid.get(i);
                    double currOrderQty = currOrder.getSize();
                    //ToDo: refactor

                    //Case where current order can completely fill crossing order, with some left over in
                    //the current order.
                    if (currOrderQty > orderSize) {
                        System.out.println("case 1");
                        currOrder.setSize(currOrderQty - orderSize);
                        order.setSize(0);
                        order.setFilled(true);
                        resetOrdersAndPriceLevels(ordersAtBestAskBid, i, currPriceLevel);
                        printSnapshot();
                        return;
                    }
                    //Case where current order exactly fill crossing order
                    else if (currOrderQty == orderSize) {
                        System.out.println("case 2");
                        resetOrdersAndPriceLevels(ordersAtBestAskBid, i + 1, currPriceLevel);
                        order.setSize(0);
                        currOrder.setSize(0);
                        order.setFilled(true);
                        currOrder.setFilled(true);
                        printSnapshot();
                        return;
                    }
                    //Case where current order only partially fills
                    //crossing order.
                    else {
                        System.out.println("case 3");
                        order.setSize(orderSize - currOrderQty);
                        orderSize = order.getSize();
                        currOrder.setSize(0);
                        currOrder.setFilled(true);
                        ordersToRemove.add(currOrder);
                        printSnapshot();
                    }
                }
                ordersAtBestAskBid.removeAll(ordersToRemove);
                //Continue to next price level. Reset best ask.
                if(ordersAtBestAskBid.isEmpty()){
                    deletePriceLevel(currPriceLevel);
                }
                currPriceLevel = getNewPriceLevel(order);
                ordersAtBestAskBid = priceLevels.get(currPriceLevel);
            }
            if (order.getSize() > 0) {
                //If there's still volume remaining add the order to the price level
                addOrderAtPriceLevel(order, orderPrice);
                printSnapshot();
            }
        }
        else {
            addOrderAtPriceLevel(order, orderPrice);
            printSnapshot();
        }
    }

    /**
     * Helper to clear orders at a given price level
     * @param ordersAtBestAskBid orders at a given price level
     * @param i index in ordersAtBestAskBid to potentially sublist and clear
     * @param currPriceLevel current priceLevel
     */
    private void resetOrdersAndPriceLevels(ArrayList<Order> ordersAtBestAskBid, int i, double currPriceLevel) {
        if(i == ordersAtBestAskBid.size()){
            deletePriceLevel(currPriceLevel);
        }
        else {
            ordersAtBestAskBid.subList(0, i).clear();
        }
    }

    private void deletePriceLevel(Double currPriceLevel) {
        priceLevels.remove(currPriceLevel);
    }

    /**
     * Helper to print the orderbook snapshot.
     */
    public void printSnapshot(){
        System.out.println("------------BEGIN SNAPSHOT------");
        for(HashMap.Entry<Double, ArrayList<Order>> en: priceLevels.entrySet()){
            System.out.println("Price Level " + en.getKey());
            for(Order o: en.getValue()){
                System.out.println("Order " + o.getOrderBookId() + " " + getBid(o) + " size: " + o.getSize());
            }
        }
        System.out.println("------------END SNAPSHOT------");

    }

    /**
     *
     * @param orderPrice price of potentially crossing order
     * @param crossedPrice price being crossed
     * @param bid if order type is bid or ask.
     * @return
     */
    private boolean validLimitCross(Double orderPrice, Double crossedPrice, boolean bid){
        return (crossedPrice <= orderPrice && bid) || (crossedPrice >= orderPrice && !bid);
    }



    /**
     * Returns the best bid in the order book.
     */
    public Double getBestBid() {
        return bidPrices.peek();
    }

    /**
     * Returns the best ask in the order book
     */
    public Double getBestAsk() {
        return askPrices.peek();
    }

    public String getBid(Order o){
        if(o.isBid()){
            return "BUY";
        }
        return "SELL";
    }

    public boolean priceLevelExists(Double priceLevel){
        return priceLevels.containsKey(priceLevel);
    }

    /**
     * Removes best bid/ask from priority queue
     * and sets this to be the new price level.
     * @param order Order to get new price level for
     * @return updated PriceLevel
     */
    private Double getNewPriceLevel(Order order){
        Double newPriceLevel;
        if(order.isBid()){
            askPrices.poll();
            if(asksExist()) {
                newPriceLevel = getBestAsk();
            }
            else {
                newPriceLevel = order.getPrice();
            }
        }
        else{
            bidPrices.poll();
            if(bidsExist()){
                newPriceLevel = getBestBid();
            }
            else {
                newPriceLevel = order.getPrice();
            }

        }
        return newPriceLevel;
    }

    /**
     *
     * @return true if bids exist
     */
    public boolean bidsExist(){
        System.out.println("There are " + bidPrices.size() + " bids");
        return bidPrices.size() > 0;
    }

    /**
     *
     * @return true if asks exist
     */
    public boolean asksExist(){
        System.out.println("There are " + askPrices.size() + " asks");
        return askPrices.size() > 0;
    }

}