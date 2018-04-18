package orderbook;

import models.entities.Order;
import models.entities.Trade;
import structures.TradeManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class OrderBook {

    private final String ticker;
    private final long maxDepth;
    private static long ORDER_ID_COUNT;

    HashMap < BigDecimal, ArrayList<Order>> priceLevels;

    //Want max heap for bids
    private PriorityQueue < BigDecimal > bidPrices;

    //Min heap for asks
    private PriorityQueue < BigDecimal > askPrices;

    private TradeManager manager;

    private HashSet<Trade> currentTrades;
    


    public OrderBook(String ticker, long maxDepth, TradeManager manager) {
        this.ticker = ticker;
        this.maxDepth = maxDepth;
        this.bidPrices = new PriorityQueue<>(Collections.reverseOrder());
        this.askPrices = new PriorityQueue<>();
        this.priceLevels = new HashMap<>();
        this.manager = manager;
        this.currentTrades = new HashSet<>();
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
        BigDecimal orderPrice = order.getPrice();
        BigDecimal orderSize = order.getSize();
        boolean isBid = order.isBid();
        BigDecimal currPriceLevel;
        if (isBid) {
            currPriceLevel = getBestAsk();
        } else {
            currPriceLevel = getBestBid();
        }
        order.setOrderbookId(ORDER_ID_COUNT++);
        try {
            processAtPriceLevel(order, currPriceLevel,orderPrice, orderSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param order Adds order to a price level.
     * @param price Price level to add order.
     */
    private void addOrderAtPriceLevel(Order order, BigDecimal price) {
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
    private void processAtPriceLevel(Order order, BigDecimal currPriceLevel, BigDecimal orderPrice, BigDecimal orderSize) throws SQLException {
        try{
            ArrayList<Order> ordersToRemove = new ArrayList<>();
            boolean selfTrade = false;
            if (currPriceLevel != null && validLimitCross(orderPrice, currPriceLevel, order.isBid())) {
                ArrayList < Order > ordersAtBestAskBid = priceLevels.get(currPriceLevel);
                while (ordersAtBestAskBid != null && validLimitCross(orderPrice, currPriceLevel, order.isBid()) && !selfTrade) {
                    //ToDo: Instead of printing snapshot, generate TradeReports.
                    for (int i = 0; i < ordersAtBestAskBid.size(); i++) {
                        Order currOrder = ordersAtBestAskBid.get(i);
                        //Prevent self trading
                        if(currOrder.getTraderId() == order.getTraderId()){
                            selfTrade = true;
                            continue;
                        }
                        BigDecimal currOrderQty = currOrder.getSize();
                        //ToDo: refactor this code is really bad
                        //Case where current order can completely fill crossing order, with some left over in
                        //the current order.
                        if (currOrderQty.compareTo(orderSize) > 0) {
                            currOrder.setSize(currOrderQty.subtract(orderSize));
                            order.setSize(BigDecimal.ZERO);
                            order.setFilled(true);
                            resetOrdersAndPriceLevels(ordersAtBestAskBid, i, currPriceLevel);
                            printSnapshot();
                            currentTrades.add(new Trade(currOrder.getTraderId(), order.getTraderId(),
                                                        currOrder.getGlobalOrderId(), order.getGlobalOrderId(),
                                                        currPriceLevel, orderSize, currOrder.getTicker()));
                            return;
                        }
                        //Case where current order exactly fill crossing order
                        else if (currOrderQty == orderSize) {
                            resetOrdersAndPriceLevels(ordersAtBestAskBid, i + 1, currPriceLevel);
                            order.setSize(BigDecimal.ZERO);
                            currOrder.setSize(BigDecimal.ZERO);
                            order.setFilled(true);
                            currOrder.setFilled(true);
                            printSnapshot();
                            currentTrades.add(new Trade(currOrder.getTraderId(), order.getTraderId(),
                                                        currOrder.getGlobalOrderId(), order.getGlobalOrderId(),
                                                        currPriceLevel, orderSize, currOrder.getTicker()));
                            return;
                        }
                        //Case where current order only partially fills
                        //crossing order.
                        else {
                            order.setSize(orderSize.subtract(currOrderQty));
                            orderSize = order.getSize();
                            currOrder.setSize(BigDecimal.ZERO);
                            currOrder.setFilled(true);
                            ordersToRemove.add(currOrder);
                            printSnapshot();
                            currentTrades.add(new Trade(currOrder.getTraderId(), order.getTraderId(),
                                                        currOrder.getGlobalOrderId(), order.getGlobalOrderId(),
                                                        currPriceLevel, orderSize, currOrder.getTicker()));
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
                if (!order.isFilled()) {
                    //If there's still volume remaining add the order to the price level
                    addOrderAtPriceLevel(order, orderPrice);
                    printSnapshot();
                }
            }
            else {
                addOrderAtPriceLevel(order, orderPrice);
                printSnapshot();
            }
        } finally {
            this.manager.manageTrades(currentTrades);

        }

    }

    /**
     * Helper to clear orders at a given price level
     * @param ordersAtBestAskBid orders at a given price level
     * @param i index in ordersAtBestAskBid to potentially sublist and clear
     * @param currPriceLevel current priceLevel
     */
    private void resetOrdersAndPriceLevels(ArrayList<Order> ordersAtBestAskBid, int i, BigDecimal currPriceLevel) {
        if(i == ordersAtBestAskBid.size()){
            deletePriceLevel(currPriceLevel);
        }
        else {
            ordersAtBestAskBid.subList(0, i).clear();
        }
    }

    private void deletePriceLevel(BigDecimal currPriceLevel) {
        priceLevels.remove(currPriceLevel);
    }

    /**
     * Helper to print the orderbook snapshot.
     */
    public void printSnapshot(){
        System.out.println("------------BEGIN SNAPSHOT------");
        for(HashMap.Entry<BigDecimal, ArrayList<Order>> en: priceLevels.entrySet()){
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
    private boolean validLimitCross(BigDecimal orderPrice, BigDecimal crossedPrice, boolean bid){
        return (crossedPrice.compareTo(orderPrice) <= 0 && bid)
                || (crossedPrice.compareTo(orderPrice) >= 0 && !bid);
    }



    /**
     * Returns the best bid in the order book.
     */
    public BigDecimal getBestBid() {
        return bidPrices.peek();
    }

    /**
     * Returns the best ask in the order book
     */
    public BigDecimal getBestAsk() {
        return askPrices.peek();
    }

    public String getBid(Order o){
        if(o.isBid()){
            return "BUY";
        }
        return "SELL";
    }

    public boolean priceLevelExists(BigDecimal priceLevel){
        return priceLevels.containsKey(priceLevel);
    }

    /**
     * Removes best bid/ask from priority queue
     * and sets this to be the new price level.
     * @param order Order to get new price level for
     * @return updated PriceLevel
     */
    private BigDecimal getNewPriceLevel(Order order){
        BigDecimal newPriceLevel;
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
//        System.out.println("There are " + bidPrices.size() + " bids");
        return bidPrices.size() > 0;
    }

    /**
     *
     * @return true if asks exist
     */
    public boolean asksExist(){
//        System.out.println("There are " + askPrices.size() + " asks");
        return askPrices.size() > 0;
    }

    public synchronized HashSet<Trade> getCurrentTrades(){
        return currentTrades;
    }

}