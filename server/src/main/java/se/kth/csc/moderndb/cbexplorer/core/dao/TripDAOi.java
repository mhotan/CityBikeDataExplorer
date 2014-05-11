package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public List<Trip> findTripByID(long bikeID, Date startDate);

    @Transactional
    public List<Trip> findTripSpecifiedByUserCharacteristics(UserParameters userParameters);

    @Transactional
    public List<Trip> findTripWithDistanceBetween(TripParameters tripParameters);

    @Transactional
    public List<Trip> findTripWithDurationBetween(TripParameters tripParameters);

    @Transactional
    public List<Trip> findTripWithinTimeRange(TripParameters tripParameters);

    @Transactional
    public List<Trip> findTripWithBikes(TripParameters tripParameters);

    @Transactional
    public List<Trip> findTripWithStartStations(TripParameters tripParameters);

    @Transactional
    public List<Trip> findTripWithEndStations(TripParameters tripParameters);
}
