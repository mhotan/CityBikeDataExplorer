package se.kth.csc.moderndb.cbexplorer.core.events.bikes;

import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.events.ReadEvent;

/**
 * Created by mhotan on 5/7/14.
 */
public class BikeEvent extends ReadEvent {

    private long bikeId;

    private Bike bike;

    public BikeEvent(long bikeId) {
        this.bikeId = bikeId;
    }

    public BikeEvent(Bike bike) {
        this.bikeId = bike.getBikeId();
        this.bike = bike;
    }

    public long getBikeId() {
        return bikeId;
    }

    public Bike getBike() {
        return bike;
    }

    public static BikeEvent notFound(long bikeId) {
        BikeEvent ev = new BikeEvent(bikeId);
        ev.entityFound = false;
        return ev;
    }
}
