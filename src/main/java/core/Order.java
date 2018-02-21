package core;


/**
 * Represents an Order in the Order book.
 */
public abstract class Order {

     private long traderId;
     private String ticker;
     private double size;
     private boolean bid;

    /**
     * @param traderId Source trader making the order
     * @param ticker Ticker (string representation of asset)
     * @param size The size of the order in number of coins
     * @param bid Boolean representing if the order is a bid or ask. True if bid.
     */
     public Order(long traderId, String ticker, double size, boolean bid){
         if(ticker == null || ticker.isEmpty()){
                 throw new IllegalArgumentException("Cannot construct order with null or empty ticker");
         }
         if(size <= 0){
             throw new IllegalArgumentException("Cannot construct order with negative/zero size");
         }
         this.traderId = traderId;
         this.ticker = ticker;
         this.size = size;
         this.bid = bid;
     }

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

}
