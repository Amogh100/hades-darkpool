package eventprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.StringRpcServer;
import models.OrderType;
import models.entities.Order;
import models.messages.ApiMessage;
import orderbook.OrderBook;
import orderbook.OrderBookFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * RPC server listening on RabbitMQ
 * for String messages containing order information,
 * and processes each of these messages.
 */
public class OrderRpcServer extends StringRpcServer {

    private ObjectMapper orderSerializer;

    //Map between ticker and an order book
    private HashMap<String, OrderBook> orderBookMap;

    /**
     * @param channel an abstraction of a connection to an AMPQ broker.
     * @param queueName Name of RabbitMQ queue for listening to orders
     * @throws IOException
     */
    public OrderRpcServer(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
        this.orderSerializer = new ObjectMapper();
        HashSet<String> tickers = new HashSet<>(Arrays.asList("BTC", "ETH", "LTC", "XRP"));
        orderBookMap = new HashMap();
        for(String ticker: tickers){
            orderBookMap.put(ticker, OrderBookFactory.createNewOrderbook(ticker, 100));
        }
    }

    /**
     *Processes a serialized order
     * @param orderMessage String message containing order info
     * @return
     */
    @Override
    public String handleStringCall(String orderMessage) {
        System.out.println("Got request: " + orderMessage);
        try {
            Order order = orderSerializer.readValue(orderMessage, Order.class);
            ApiMessage resp = OrderValidator.validate(order, orderBookMap.keySet());
            if(resp.getSuccess()){
                OrderBook book = orderBookMap.get(order.getTicker());
                if(order.getType() == OrderType.MARKET && (!book.bidsExist() || !book.asksExist())){
                    resp.setSuccess(false);
                    resp.setMessage("Can't place market orders when there are no bids or no asks!");
                }
                else{
                    book.addOrder(order);
                }
            }
            return orderSerializer.writeValueAsString(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
