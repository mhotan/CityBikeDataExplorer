package se.kth.csc.moderndb.cbexplorer.core.services;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.TimeRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.stats.StationUsageStatistics;

import java.util.List;
import java.util.Map;

/**
 * Service level abstractions for making the underlying table structure transparent.
 *
 * Created by mhotan on 5/12/14.
 */
public interface RelationalService {

    /**
     * @return The time range from the earliest to latest trip
     */
    @Transactional
    public TimeRange getTimeRange();

    @Transactional
    public Map<Long, StationUsageStatistics> getStationStatistics(long stationId);

    /**
     * @return all the bikes available
     */
    @Transactional
    public List<Bike> getAllBikes();

    /**
     * @return All the stations.
     */
    @Transactional
    public List<Station> getAllStations();

    /**
     * Return the time bounds where all the trips fall in.
     *
     * @return the bounds between the entire trip times.
     */
    @Transactional
    public TimeRange getTripTimeLimits();

    /**
     * @return the limits for all the possible trip durations
     */
    @Transactional
    public IntegerRange getDurationLimits();

    /**
     * @return Birth Year limits.
     */
    @Transactional
    public ShortRange getBirthYearLimits();

    /**
     * @return all the possible user types.
     */
    @Transactional
    public List<String> getUserTypes();



}
