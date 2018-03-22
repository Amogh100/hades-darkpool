package models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.OrderType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Represents an Order in the Order book.
 */
@Entity
@Table(name="orders")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @Id
    @GeneratedValue
    private long globalOrderId;

    @JsonIgnore
    private long orderBookId;

    @JsonProperty(required = true)
    private long traderId;

    @JsonProperty(required = true)
    private String ticker;

    @JsonProperty(required = true)
    private double size;

    @JsonProperty(required = true)
    private boolean bid;

    @JsonProperty(required = true)
    private OrderType type;


    @JsonProperty(required = true)
    private double price;

    @JsonIgnore
    private boolean filled;

    private Date date;


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
        this.globalOrderId = orderId;
        this.filled = false;
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

    public long getGlobalOrderId(){return globalOrderId;}

    public void setGlobalOrderId(long globalOrderId) {
        this.globalOrderId = globalOrderId;
    }

    public void setTraderId(long traderId) {
        this.traderId = traderId;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setBid(boolean bid) {
        this.bid = bid;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void setOrderbookId(long orderBookId) {this.orderBookId = orderBookId;}

    public long getOrderBookId() {
        return orderBookId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate(){
        return date;
    }
}
