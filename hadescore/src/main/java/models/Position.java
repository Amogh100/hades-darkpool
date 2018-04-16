package models;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Class for representing a position in a given asset
 */

@Entity
public class Position {


    private String assetId;
    private BigDecimal positionSize;
    private long traderId;
    
    public String getAssetId(){
        return assetId;
    }

    public void updatePositionSize(BigDecimal change){positionSize.add(change);}

    public Position(String assetId, BigDecimal positionSize, long traderId){
        this.assetId = assetId;
        this.positionSize = positionSize;
        this.traderId = traderId;
    }

    public BigDecimal getPositionSize() {
        return positionSize;
    }

}