package structures;

import eventprocessing.TradeDatabaseUpdater;
import models.entities.Trade;

import java.util.Set;
import java.util.concurrent.*;

public class TradeCache {

    private Set<Trade> tradeSet;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public TradeCache(){
        this.tradeSet = ConcurrentHashMap.newKeySet();
        scheduler.scheduleAtFixedRate(new TradeDatabaseUpdater(this), 100, 100, TimeUnit.MILLISECONDS);
    }

    public synchronized Trade[] getTrades() {
        return tradeSet.toArray(new Trade[tradeSet.size()]);
    }

    public synchronized void clearCache(){
        this.tradeSet.clear();
    }
    public synchronized void addTrade(Trade t){
        tradeSet.add(t);
    }
    public synchronized boolean isEmpty(){
        return tradeSet.isEmpty();
    }

}
