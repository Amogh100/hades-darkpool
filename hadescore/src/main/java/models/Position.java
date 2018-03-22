package models;

/**
 * Class for representing a position in a given asset
 */
public class Position {
    
    private String assetId;
    private double capital;

    public String getAssetId(){
        return assetId;
    }

    public double getCapital(){
        return capital;
    }
}