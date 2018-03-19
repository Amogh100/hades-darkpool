package models.entities;

import models.Position;

import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;
    private String password;
    private HashSet<Position> positions;

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
}
