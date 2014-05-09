package se.kth.csc.moderndb.cbexplorer.core.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * POJO TripParameters class.
 *
 * Created by Jeannine on 07.05.14.
 */
public class TripParameters {
    /**
     * Determine the user characteristics
     */
    private UserParameters userParameters;
    /**
     * Range of Trip duration
     */
    private long startOfTripDurationRange;
    private long endOfTripDurationRange;
    /**
     * Range of Trip distance
     */
    private long startOfTripDistanceRange;
    private long endOfTripDistanceRange;
    /**
     * List of examined Bike IDs
     */
    private ArrayList<Long> bikeIDs;
    /**
     * List of examined StartStations
     */
    private ArrayList<Station> startStation;
    /**
     * List of examined EndStations
     */

    private ArrayList<Station> endStation;
    /**
     * Range for the start time of the Trip
     */
    private long startOfTripStartTimeRange;
    private long endOfTripStartTimeRange;
    /**
     * Range for the end time of the Trip
     */
    private long startOfTripEndTimeRange;
    private long endOfTripEndTimeRange;

    /**
     * Empty Constructor for POJO objects
     */
    public TripParameters() {
    }

    /**
     * Creates a new TripParameters POJO object
     * @param userParameters user information
     * @param startOfTripDurationRange start of range for Trip duration
     * @param endOfTripDurationRange end of range for Trip duration
     * @param startOfTripDistanceRange start of range for Trip distance
     * @param endOfTripDistanceRange end of range for Trip distance
     * @param bikeIDs examined bike ids
     * @param startStation examined start stations
     * @param endStation examined end stations
     * @param startOfTripStartTimeRange start of range for start time
     * @param endOfTripStartTimeRange end of range for start time
     * @param startOfTripEndTimeRange start of range for end time
     * @param endOfTripEndTimeRange end of range for end time
     */
    public TripParameters(UserParameters userParameters, long startOfTripDurationRange, long endOfTripDurationRange, long startOfTripDistanceRange, long endOfTripDistanceRange, ArrayList<Long> bikeIDs, ArrayList<Station> startStation, ArrayList<Station> endStation, long startOfTripStartTimeRange, long endOfTripStartTimeRange, long startOfTripEndTimeRange, long endOfTripEndTimeRange) {
        this.userParameters = userParameters;
        this.startOfTripDurationRange = startOfTripDurationRange;
        this.endOfTripDurationRange = endOfTripDurationRange;
        this.startOfTripDistanceRange = startOfTripDistanceRange;
        this.endOfTripDistanceRange = endOfTripDistanceRange;
        this.bikeIDs = bikeIDs;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startOfTripStartTimeRange = startOfTripStartTimeRange;
        this.endOfTripStartTimeRange = endOfTripStartTimeRange;
        this.startOfTripEndTimeRange = startOfTripEndTimeRange;
        this.endOfTripEndTimeRange = endOfTripEndTimeRange;
    }

    public UserParameters getUserParameters() {
        return userParameters;
    }

    public long getStartOfTripDurationRange() {
        return startOfTripDurationRange;
    }

    public long getEndOfTripDurationRange() {
        return endOfTripDurationRange;
    }

    public long getStartOfTripDistanceRange() {
        return startOfTripDistanceRange;
    }

    public long getEndOfTripDistanceRange() {
        return endOfTripDistanceRange;
    }

    public ArrayList<Long> getBikeIDs() {
        return bikeIDs;
    }

    public ArrayList<Station> getStartStation() {
        return startStation;
    }

    public ArrayList<Station> getEndStation() {
        return endStation;
    }

    public long getStartOfTripStartTimeRange() {
        return startOfTripStartTimeRange;
    }

    public long getEndOfTripStartTimeRange() {
        return endOfTripStartTimeRange;
    }

    public long getStartOfTripEndTimeRange() {
        return startOfTripEndTimeRange;
    }

    public long getEndOfTripEndTimeRange() {
        return endOfTripEndTimeRange;
    }
}
