package models;

import javax.persistence.Entity;

/**
 * Class for representing a position in a given asset
 */

@Entity
public class Position {


    private String assetId;
    private double positionSize;
    private long traderId;
    
    public String getAssetId(){
        return assetId;
    }

    public void updatePositionSize(double change){positionSize += change;}

    public Position(String assetId, double positionSize, long traderId){
        this.assetId = assetId;
        this.positionSize = positionSize;
        this.traderId = traderId;
    }

    public double getPositionSize() {
        return positionSize;
    }

}