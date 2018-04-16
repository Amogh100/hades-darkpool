package models.entities;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Account that stores capital information
 */
@Entity
public class Account {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Trader trader;

    private BigDecimal capital;

    public BigDecimal getCapital() {
        return capital;
    }

    public long getId() {
        return id;
    }

    public void addCapital(BigDecimal newCapital) {
        capital.add(newCapital);
    }


    public Account(){
        this(BigDecimal.valueOf(100000));
    }

    public Account(BigDecimal capital){
        this.capital = capital;
    }
}
