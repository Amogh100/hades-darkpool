package eventprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.StringRpcServer;
import models.entities.Order;

import java.io.IOException;

public class OrderListener {
    public static void main(String[] args) {
        try {

            ConnectionFactory connFactory = new ConnectionFactory();
            connFactory.setHost("172.17.0.2");
            Connection conn = connFactory.newConnection();
            final Channel ch = conn.createChannel();
            final ObjectMapper serializer=  new ObjectMapper();

            ch.queueDeclare("order", false, false, false, null);
            OrderRpcServer server = new OrderRpcServer(ch, "order");
            server.mainloop();
        } catch (Exception ex) {
            System.err.println("Main thread caught exception: " + ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}