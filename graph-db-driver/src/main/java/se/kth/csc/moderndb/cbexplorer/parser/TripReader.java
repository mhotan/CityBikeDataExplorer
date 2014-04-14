package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.domain.Station;
import se.kth.csc.moderndb.cbexplorer.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.util.Collection;

/**
 * Created by mhotan on 4/13/14.
 */
public class TripReader implements CitiBikeReader {
    @Override
    public void addTrips(Collection<TripData> trips) {
        for (TripData tripData : trips) {
            Trip trip = new Trip(tripData.getStartTime(), tripData.getEndTime(), tripData.getUserType(),
                    tripData.getUserBirthYear(), tripData.getUserGenderAsString());
            Bike bike; // TODO find or create these nodes as necessary
            Station startStation, endStation;
        }
    }
}
