package se.kth.csc.moderndb.cbexplorer.rest.controller.fixture;

import se.kth.csc.moderndb.cbexplorer.core.events.bikes.BikeEvent;

/**
 * Created by mhotan on 5/7/14.
 */
public class RestEventFixtures {

    public static BikeEvent bikeNotFound(long bikeId) {
        return BikeEvent.notFound(bikeId);
    }
    public static BikeEvent bikeEvent(long bikeId) {
        return new BikeEvent(RestDataFixture.customBike(bikeId));
    }

}
