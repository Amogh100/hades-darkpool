package core;

public class LimitOrder extends Order {

    private double price;
    /**
     * @param traderId Source trader making the order
     * @param ticker   Ticker (string representation of asset)
     * @param size     The size of the order in number of coins
     * @param bid      Boolean representing if the order is a bid or ask. True if bid.
     */
    public LimitOrder(long traderId, String ticker, double size, boolean bid, double price) {
        super(traderId, ticker, size, bid);
        if(price <= 0){
            throw new IllegalArgumentException("Cannot construct limit order with price less than 0");
        }
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

}
