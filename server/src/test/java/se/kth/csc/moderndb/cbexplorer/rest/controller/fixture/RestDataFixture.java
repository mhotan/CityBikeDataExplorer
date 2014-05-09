package se.kth.csc.moderndb.cbexplorer.rest.controller.fixture;

import se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.AllBikesEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing data fixture.
 *
 * Created by mhotan on 5/7/14.
 */
public class RestDataFixture {

    public static final long TEST_ID = 1L;

    public static AllBikesEvent allBikes() {
        List<Bike> bikes = new ArrayList<Bike>();
        bikes.add(standardBike());
        bikes.add(standardBike());
        bikes.add(standardBike());
        return new AllBikesEvent(bikes);
    }

    public static Bike standardBike() {
        return new Bike(TEST_ID);
    }

    public static Bike customBike(long id) {
        return new Bike(id);
    }



}
