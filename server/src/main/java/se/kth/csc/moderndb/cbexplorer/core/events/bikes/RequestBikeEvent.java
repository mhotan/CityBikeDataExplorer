package se.kth.csc.moderndb.cbexplorer.core.events.bikes;

import se.kth.csc.moderndb.cbexplorer.core.events.RequestReadEvent;

/**
 * Created by mhotan on 5/7/14.
 */
public class RequestBikeEvent extends RequestReadEvent {

    /**
     * The Bike Id to retrieve.
     */
    private long bikeId;

    /**
     * @param bikeId Bike Id to retrieve.
     */
    public RequestBikeEvent(long bikeId) {
        this.bikeId = bikeId;
    }

    public long getBikeId() {
        return bikeId;
    }
}
