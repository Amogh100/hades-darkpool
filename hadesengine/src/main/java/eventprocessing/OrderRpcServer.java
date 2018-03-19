package eventprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.StringRpcServer;
import models.entities.Order;
import models.messages.ApiMessage;
import orderbook.OrderBook;
import orderbook.OrderBookFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class OrderRpcServer extends StringRpcServer {

    private ObjectMapper orderSerializer;

    private HashMap<String, OrderBook> orderBookMap;

    public OrderRpcServer(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
        this.orderSerializer = new ObjectMapper();
        HashSet<String> tickers = new HashSet<>(Arrays.asList("BTC", "ETH", "LTC", "XRP"));
        orderBookMap = new HashMap();
        for(String ticker: tickers){
            orderBookMap.put(ticker, OrderBookFactory.createNewOrderbook(ticker, 100));
        }
    }

    public String handleStringCall(String request) {
        System.out.println("Got request: " + request);
        try {
            Order order = orderSerializer.readValue(request, Order.class);
            ApiMessage resp = OrderValidator.validate(order, orderBookMap.keySet());
            if(resp.getSuccess()){
                orderBookMap.get(order.getTicker()).addOrder(order);
            }
            return orderSerializer.writeValueAsString(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
