package se.kth.csc.moderndb.cbexplorer.core.dataAccessObject;

import se.kth.csc.moderndb.cbexplorer.core.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.core.domain.TripParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.UserParameters;

import java.util.Date;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface TripDAOi {

    public Trip findTripByID(long bikeID, Date startDate);

    public Trip findTripSpecifiedByUserCharacteristics(UserParameters userParameters);

    public Trip findTripWithDistanceBetween(TripParameters tripParameters);

    public Trip findTripWithDurationBetween(TripParameters tripParameters);

    public Trip findTripWithinTimeRange(TripParameters tripParameters);

    public Trip findTripWithBikes(TripParameters tripParameters);

    public Trip findTripWithStartStations(TripParameters tripParameters);

    public Trip findTripWithEndStations(TripParameters tripParameters);
}
