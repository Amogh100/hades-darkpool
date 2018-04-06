package models.entities;

import javax.persistence.*;

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



    private double capital;

    public double getCapital() {
        return capital;
    }

    public long getId() {
        return id;
    }

    public void addCapital(double newCapital) {
        capital += newCapital;
    }


    public Account(){
        this(100000);
    }

    public Account(double capital){
        this.capital = capital;
    }
}
