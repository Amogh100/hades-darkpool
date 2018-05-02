package structures;

import dao.OrderDao;
import models.entities.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OpenOrderCache {

    private static ConcurrentHashMap<Long, ArrayList<Order>> openOrders = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //load Populates the open order cache from the database.
    public void load(){
        openOrders = new ConcurrentHashMap<>(OrderDao.getAllOpenOrders());
        scheduler.scheduleAtFixedRate(new Runnable() {
            //Periodically remove filled orders from the cache.
            @Override
            public void run() {
                Iterator it = openOrders.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry)it.next();
                    ArrayList<Order> orders = (ArrayList<Order>)entry.getValue();
                    // Remove the current element from the iterator and the list.
                    orders.removeIf(Order::isFilled);
                }
            }
        }, 10, 10, TimeUnit.MICROSECONDS);

    }

    //Returns all open orders.
    public synchronized static ConcurrentHashMap<Long, ArrayList<Order>> getOpenOrders(){
        return openOrders;
    }
}
