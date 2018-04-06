package models;


/**
 * Class for representing a position in a given asset
 */
public class Position {


    private String assetId;
    private double positionSize;

    public String getAssetId(){
        return assetId;
    }

    public void updatePositionSize(double change){positionSize += change;}

    public Position(String assetId, double positionSize){
        this.assetId = assetId;
        this.positionSize = positionSize;
    }

    public double getPositionSize() {
        return positionSize;
    }

}