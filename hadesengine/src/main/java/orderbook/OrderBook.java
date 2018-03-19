package orderbook;

import models.entities.Order;

import java.util.*;

public class OrderBook {

    private final String ticker;
    private final long maxDepth;

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
    }

    /**
     * Simply add order to bids and asks depending on order type.
     *
     * @Param Order order to add to order book.
     * ToDo: Actual matching algorithm, and order book for each asset.
     * ToDo: Updgrade market orders so that their price is updated to either the best bid or ask.
     *
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

    private void handleMarketOrder(Order order) {
        if (order.isBid()) {
            order.setPrice(getBestAsk());
        } else {
            order.setPrice(getBestBid());
        }
        handleLimitOrder(order);
    }

    private void handleLimitOrder(Order order) {
        double orderPrice = order.getPrice();
        double orderSize = order.getSize();
        boolean isBid = order.isBid();
        Double currPriceLevel = null;
        if (isBid) {
            currPriceLevel = getBestAsk();
        } else {
            currPriceLevel = getBestBid();
        }
        processAtPriceLevel(order, currPriceLevel,orderPrice, orderSize);
    }

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

    private void processAtPriceLevel(Order order, Double currPriceLevel, Double orderPrice, Double orderSize) {
        if (currPriceLevel != null && ((currPriceLevel <= orderPrice && order.isBid()) ||
                                        (currPriceLevel >= orderPrice && !order.isBid()))) {
            ArrayList < Order > ordersAtBestAskBid = priceLevels.get(currPriceLevel);
            System.out.println("HERE!");
            while (ordersAtBestAskBid != null && ((currPriceLevel <= orderPrice && order.isBid()) ||
                                               (currPriceLevel >= orderPrice && !order.isBid()))) {
                //ToDo: Instead of printing snapshot, generate TradeReports.
                for (int i = 0; i < ordersAtBestAskBid.size(); i++) {
                    Order currOrder = ordersAtBestAskBid.get(i);
                    double currOrderQty = currOrder.getSize();
                    if (currOrderQty > orderSize) {
                        System.out.println("CASE 1");
                        currOrder.setSize(currOrderQty - orderSize);
                        order.setSize(0);
                        ordersAtBestAskBid = new ArrayList<>(ordersAtBestAskBid.subList(i, ordersAtBestAskBid.size()));
                        priceLevels.put(currPriceLevel, ordersAtBestAskBid);
                        printSnapshot();
                        return;
                    } else if (currOrderQty == orderSize) {
                        System.out.println("CASE 2");
                        ordersAtBestAskBid = new ArrayList<>(ordersAtBestAskBid.subList(i + 1, ordersAtBestAskBid.size()));
                        priceLevels.put(currPriceLevel, ordersAtBestAskBid);
                        order.setSize(0);
                        currOrder.setSize(0);
                        printSnapshot();
                        return;
                    } else {
                        System.out.println("CASE 3");
                        order.setSize(orderSize - currOrderQty);
                        currOrder.setSize(0);
                        printSnapshot();
                    }
                }
                //Continue to next price level. Reset best ask.
                if(ordersAtBestAskBid.isEmpty()){
                    priceLevels.remove(currPriceLevel);
                }
                if(order.isBid()){
                    askPrices.poll();
                    currPriceLevel = getBestAsk();
                }
                else{
                    bidPrices.poll();
                    currPriceLevel = getBestBid();
                }
                ordersAtBestAskBid = priceLevels.get(currPriceLevel);
            }
            if (orderSize > 0) {
                addOrderAtPriceLevel(order, orderPrice);
                printSnapshot();
            }
        }
        else {
            addOrderAtPriceLevel(order, orderPrice);
            printSnapshot();
        }
    }

    public void printSnapshot(){
        for(HashMap.Entry<Double, ArrayList<Order>> en: priceLevels.entrySet()){
            System.out.println("Price Level " + en.getKey());
            for(Order o: en.getValue()){
                System.out.println("Order " + o.getOrderId() + " " + getBid(o) + " size: " + o.getSize());
            }
        }
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


}