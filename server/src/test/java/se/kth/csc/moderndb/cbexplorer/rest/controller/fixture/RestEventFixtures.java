package se.kth.csc.moderndb.cbexplorer.rest.controller.fixture;

import se.kth.csc.moderndb.cbexplorer.core.events.bikes.BikeEvent;

import java.util.UUID;

/**
 * Created by mhotan on 5/7/14.
 */
public class RestEventFixtures {

    public static BikeEvent orderDetailsNotFound(long bikeId) {
        return BikeEvent.notFound(bikeId);
    }
    public static BikeEvent orderDetailsEvent(long bikeId) {
        return new BikeEvent(bikeId, customKeyOrderDetails(key));
    }

}
