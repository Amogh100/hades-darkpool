package structures;

import eventprocessing.TradeDatabaseUpdater;
import models.entities.Trade;

import java.util.Set;
import java.util.concurrent.*;

public class TradeCache {

    private Set<Trade> tradeSet;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    //Constructs a TradeCache
    public TradeCache(){
        this.tradeSet = ConcurrentHashMap.newKeySet();
        //Schedule a DB update every 100 milliseconds.
        scheduler.scheduleAtFixedRate(new TradeDatabaseUpdater(this), 100, 100, TimeUnit.MILLISECONDS);
    }

    //getTrades retrieves all the Trades in the Cache
    public synchronized Trade[] getTrades() {
        return tradeSet.toArray(new Trade[tradeSet.size()]);
    }

    //clearCache removes all the Trades from the Cache
    public synchronized void clearCache(){
        System.out.println("Clearing cache");
        this.tradeSet.clear();
    }
    
    /**
      * Adds a Trade to the cache.
      * @Param t Trade to add the cache.
      */
    public synchronized void addTrade(Trade t){
        System.out.println("Added trade to cache!");
        tradeSet.add(t);
    }
    
    
    /**
      * Returns true if the cache is empty.
      */
    public synchronized boolean isEmpty(){
        return tradeSet.isEmpty();
    }

}
