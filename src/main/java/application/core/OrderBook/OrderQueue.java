package application.core.OrderBook;

import application.core.models.Order;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OrderQueue {

    private static BlockingQueue<Order> queueInstance;


    public static BlockingQueue<Order> getQueueInstance(){
        if(queueInstance == null){
            queueInstance = new ArrayBlockingQueue<>(100);
        }
        return queueInstance;
    }
}
