package orderbook;

public class OrderBookFactory {

    /**
     * Creates a new Order Book from the given parameters
     * @param ticker Instrument for the order book
     * @param maxDepth The max depth for the given order book
     * @param queue The OrderQueue that is the source of the datOa for the order book.
     * @return
     */
    public static OrderBook createNewOrderbook(String ticker, long maxDepth){
        return new OrderBook(ticker, maxDepth);
    }

}
