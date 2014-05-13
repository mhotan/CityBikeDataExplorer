package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.core.domain.params.TripParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.params.UserParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.TimeRange;

import java.util.Date;
import java.util.List;

/**
 * Interface for the Data Access Object pattern for the Trip Table.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface TripDAOi {

    @Transactional
    public List<Bike> findAllBikes();

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
    public List<Trip> findTripWithStartStations(long stationId);

    @Transactional
    public List<Trip> findTripWithEndStations(long stationId);

    @Transactional
    public Integer countTripDeparting(long stationId);

    @Transactional
    public Integer countTripArriving(long stationId);


    /**
     * @return The time range from earliest to latest trips.
     */
    public TimeRange getTripTimeLimits();

    /**
     * @return The duration range limits of the shortest to longest trips.
     */
    public IntegerRange getTripDurationLimits();

    /**
     * @return The birth year limits for all the users.
     */
    public ShortRange getUserBirthYearLimits();

    /**
     * @return All the user types available.
     */
    public List<String> getUserTypes();

}
