package se.kth.csc.moderndb.cbexplorer.core.dataAccessObject;

import se.kth.csc.moderndb.cbexplorer.core.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.core.domain.TripParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.UserParameters;

import java.util.Date;
import java.util.List;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface TripDAOi {

    public List<Trip> findTripByID(long bikeID, Date startDate);

    public List<Trip> findTripSpecifiedByUserCharacteristics(UserParameters userParameters);

    public List<Trip> findTripWithDistanceBetween(TripParameters tripParameters);

    public List<Trip> findTripWithDurationBetween(TripParameters tripParameters);

    public List<Trip> findTripWithinTimeRange(TripParameters tripParameters);

    public List<Trip> findTripWithBikes(TripParameters tripParameters);

    public List<Trip> findTripWithStartStations(TripParameters tripParameters);

    public List<Trip> findTripWithEndStations(TripParameters tripParameters);
}
