package application.core.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderQueueService {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "order";
    private String replyQueueName;
    private RpcClient service;

    private static  OrderQueueService orderQueueService = null;

    private OrderQueueService() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.17.0.2");

        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        service = new RpcClient(channel, "", requestQueueName);
    }

    public static OrderQueueService getServiceInstance(){
        if(orderQueueService == null){
            try {
                orderQueueService = new OrderQueueService();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return orderQueueService;
    }

    public String processOrder(String orderString) throws IOException, TimeoutException {
        try {
            String res = service.stringCall(orderString);
            return res;
        } catch (TimeoutException e) {
            System.out.println("Service took to long to respond");
            e.printStackTrace();
        }
        return "";
    }
}
