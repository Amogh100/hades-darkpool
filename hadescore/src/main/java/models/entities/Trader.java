package models.entities;

import models.Position;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Trader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;


    private String username;
    private String password;

    private HashSet<Position> positions;

    @OneToOne(cascade = {CascadeType.ALL})
    public Account account;

    public Trader(){}

    public Trader(String username, String password){
        this.username = username;
        this.password = password;
        this.positions = new HashSet<Position>();
        this.account = new Account();
    }

    public long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }


    public String getPassword(){
        return password;
    }

    public Set<Position> getPositions(){
        return positions;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void addPosition(Position pos){
        positions.add(pos);
    }

    public BigDecimal getCapital(){
        return account.getCapital();
    }

    public void addCapital(BigDecimal capital){
        account.addCapital(capital);
    }

    public Account getAccount() {
        return account;
    }
}
