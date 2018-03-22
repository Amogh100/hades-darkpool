package application.core.services;

import application.core.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.entities.Order;
import models.messages.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Service that contains main business logic
 * for adding orders and querying for relevant orders.
 * Abstracts away access to the database
 */
@Service
public class OrderService {


    private final ObjectMapper messageSerializer;

    @Autowired
    private OrderRepository orders;

    private OrderQueueService orderQueueService;

    public OrderService() throws IOException, TimeoutException {
        this.orderQueueService = OrderQueueService.getServiceInstance();
        this.messageSerializer = new ObjectMapper();
    }


    /**
     *
     * @param traderId id of the trader to get the open orders of
     * @return List of open orders entered by the trader with id traderId.
     */
    public List<Order> getOrdersForTrader(long traderId){
        return orders.findByTraderId(traderId);
    }

    /**
     * Sends a serialized order to the queueing service,
     * which sends the message over RabbitMQ
     * @param orderMessage Serialized order to process.
     * @return
     * @throws TimeoutException
     */
    public ApiMessage processOrder(String orderMessage) throws TimeoutException {
        try {
            String res = orderQueueService.processOrder(orderMessage);

            ApiMessage msg = messageSerializer.readValue(res, ApiMessage.class);

            return msg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Save order to database
    public void saveOrder(Order o) {
        orders.save(o);
    }

}
