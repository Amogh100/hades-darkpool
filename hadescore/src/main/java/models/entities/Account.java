package models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Account that stores capital information
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public Account(long capital){
        this.capital = capital;
    }
}
