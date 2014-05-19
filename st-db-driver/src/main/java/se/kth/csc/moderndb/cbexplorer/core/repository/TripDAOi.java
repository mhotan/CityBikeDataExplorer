package se.kth.csc.moderndb.cbexplorer.core.repository;

import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;
import se.kth.csc.moderndb.cbexplorer.core.params.BikeParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.TemporalParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.UserParameters;
import se.kth.csc.moderndb.cbexplorer.core.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.range.TimeRange;

import java.util.Date;
import java.util.List;

/**
 * Interface for the Data Access Object pattern for the Trip Table.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface TripDAOi {

    /**
     * @return All the Bikes that exists
     */
    public List<Bike> findAllBikes();

    /**
     * Find a bike with a given id.
     *
     * @param bikeID Bike id of bike to find
     * @return Bike with specific ID
     */
    public Bike findBikeByID(long bikeID);

    /**
     * The Bike ID and start time are used as unique keys.
     *
     * @param bikeID Id of the bike used
     * @param startDate Start Date and time of the trip
     * @return The specific trip from these parameters
     */
    public Trip findTripByID(long bikeID, Date startDate);

    /**
     * Finds all the trips based off of the given parameters.  If any of the parameters are null then
     * it will be ignored when retrieving all the trips.  Currently there is a limit for maximum number
     * of ships returned.  Therefore general queries where there are many trips being returned will be limited.
     *
     * @param startStation The station ID of the trips source station. Can be NULL.
     * @param endStation The station ID of the trips destination station. Can be NULL.
     * @param bikeParameters Parameters that define the types of bikes used on the resulting trips. Can be NULL.
     * @param userParameters Parameters that define the types of users used on the resulting trips. Can be NULL.
     * @param temporalParameters Parameters that define the time range. Can be NULL.
     * @return The resulting list of trips, or an empty list if no trip matching the requirements is available.
     */
    public List<Trip> findTrips(Long startStation, Long endStation,
                                BikeParameters bikeParameters, UserParameters userParameters,
                                TemporalParameters temporalParameters);

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
