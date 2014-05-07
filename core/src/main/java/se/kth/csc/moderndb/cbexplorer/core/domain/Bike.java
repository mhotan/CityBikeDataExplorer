package se.kth.csc.moderndb.cbexplorer.core.domain;


/**
 * POJO Bike class.
 *
 * Created by mhotan on 5/7/14.
 */
public class Bike {

    /**
     * Citibike generated unique ID.
     */
    private long bikeId;

    /**
     * Empty Constructor for POJO purposes.
     */
    public Bike() {}

    /**
     * Creates a POJO Citibike Bike entity
     *
     * @param bikeId Citibike unique ID.
     */
    public Bike(long bikeId) {
        this.bikeId = bikeId;
    }

    /**
     * @return The unique bike ID.
     */
    public long getBikeId() {
        return bikeId;
    }
}


