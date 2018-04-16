package models.entities;

import javax.persistence.*;
import java.math.BigDecimal;

//Trade entity manages a trade in the system.
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    private final long trader1Id;
    private final long trader2Id;
    private final long order1Id;
    private final long order2Id;
    private final BigDecimal fillSize;
    private final BigDecimal price;


    public Trade(){this(0, 0, 0, 0,BigDecimal.ZERO,BigDecimal.ZERO);}


    public Trade(long trader1Id, long trader2Id, long order1Id, long order2Id, BigDecimal price, BigDecimal fillSize){
        this.trader1Id = trader1Id;
        this.trader2Id = trader2Id;
        this.order1Id = order1Id;
        this.order2Id = order2Id;
        this.price = price;
        this.fillSize = fillSize;
    }


    public long getTrader1Id() {
        return trader1Id;
    }

    public long getTrader2Id() {
        return trader2Id;
    }

    public long getOrder1Id() {
        return order1Id;
    }

    public long getOrder2Id() {
        return order2Id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSize() {
        return fillSize;
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        if(!(other instanceof Trade)){
            return false;
        }
        Trade t = (Trade) other;
        boolean order1equal = t.getOrder1Id() == order1Id;
        boolean order2Equal = t.getOrder2Id() == order2Id;
        boolean trader1Equal = t.getTrader1Id() == trader1Id;
        boolean trader2Equal = t.getTrader2Id() == trader2Id;
        boolean sizeEqual = t.getSize().equals(fillSize);
        boolean priceEqual = t.getPrice().equals(price);
        return order1equal && order2Equal && trader1Equal && trader2Equal && sizeEqual && priceEqual;
    }
}
