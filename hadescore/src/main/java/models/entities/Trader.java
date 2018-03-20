package models.entities;

import models.Position;

import javax.persistence.*;
import java.util.HashSet;

@Entity
public class Trader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;
    private String password;
    private HashSet<Position> positions;

    @OneToOne
    public Account account;





    public Trader(){}

    public Trader(String username, String password){
        this.username = username;
        this.password = password;
        this.positions = new HashSet<Position>();
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

    public HashSet<Position> getPositions(){
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

    public double getCapital(){
        return account.getCapital();
    }

    public void addCapital(double capital){
        account.addCapital(capital);
    }

    public Account getAccount() {
        return account;
    }
}
