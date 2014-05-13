package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.core.dao.StationDAOi;
import se.kth.csc.moderndb.cbexplorer.core.dao.TripDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.TimeRange;

import java.util.List;

/**
 * Created by mhotan on 5/12/14.
 */
public class RelationalServiceImpl implements RelationalService {

    private final TripDAOi tripDAO;
    private final StationDAOi stationDAO;

    public RelationalServiceImpl(TripDAOi tripDAO, StationDAOi stationDAO) {
        if (tripDAO == null || stationDAO == null)
            throw new NullPointerException();
        this.tripDAO = tripDAO;
        this.stationDAO = stationDAO;
    }

    @Override
    public TimeRange getTimeRange() {
        return tripDAO.getTripTimeLimits();
    }


    // Looks a little redundant but this makes the most sense with regards to architecture and
    // abstraction.

    @Override
    public List<Bike> getAllBikes() {
        return tripDAO.findAllBikes();
    }

    @Override
    public List<Station> getAllStations() {
        return stationDAO.findAllStations();
    }

    @Override
    public TimeRange getTripTimeLimits() {
        return tripDAO.getTripTimeLimits();
    }

    @Override
    public IntegerRange getDurationLimits() {
        return tripDAO.getTripDurationLimits();
    }

    @Override
    public ShortRange getBirthYearLimits() {
        return tripDAO.getUserBirthYearLimits();
    }

    @Override
    public List<String> getUserTypes() {
        return tripDAO.getUserTypes();
    }
}
