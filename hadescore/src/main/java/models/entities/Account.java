package models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long accountId;
    private double capital;

    public double getCapital() {
        return capital;
    }

    public long getAccountId() {
        return accountId;
    }

    public void addCapital(double newCapital) {
        capital += newCapital;
    }
}
