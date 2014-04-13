package se.kth.csc.moderndb.cbexplorer.parser.data;

/**
 * Raw data read that represents Bike Data.
 *
 * Created by mhotan on 4/13/14.
 */
public class BikeData {

    /**
     * The Citibike defined ID number.
     */
    private final long id;

    /**
     * Simple constructor
     * @param id
     */
    public BikeData(long id) {
        this.id = id;
    }

    /**
     * @return The ID of this bike
     */
    public long getId() {
        return id;
    }
}
