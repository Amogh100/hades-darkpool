package structures;

import eventprocessing.TradeDatabaseUpdater;
import models.entities.Trade;

import java.util.Set;
import java.util.concurrent.*;

public class TradeCache {

    private Set<Trade> tradeSet;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean flush;

    //Constructs a TradeCache
    public TradeCache(boolean flushPeriodically){
        this.tradeSet = ConcurrentHashMap.newKeySet();
        this.flush = flushPeriodically;
        //Schedule a DB update every 100 milliseconds.
        if(flush){
            scheduler.scheduleAtFixedRate(new TradeDatabaseUpdater(this), 100, 100, TimeUnit.MILLISECONDS);
        }
    }

    //getTrades retrieves all the Trades in the Cache
    public synchronized Trade[] getTrades() {
        return tradeSet.toArray(new Trade[tradeSet.size()]);
    }

    //clearCache removes all the Trades from the Cache
    public synchronized void clearCache(){
        for(Trade t: tradeSet){
            System.out.println("Clearing " + t);
        }
        this.tradeSet.clear();
        System.out.println("Size of cache " + this + " is " + this.tradeSet.size());
    }
    
    /**
      * Adds a Trade to the cache.
      * @Param t Trade to add the cache.
      */
    public synchronized void addTrade(Trade t){
        tradeSet.add(t);
    }
    
    
    /**
      * Returns true if the cache is empty.
      */
    public synchronized boolean isEmpty(){
        return tradeSet.isEmpty();
    }

    public synchronized boolean isFlush(){
        return flush;
    }
}
