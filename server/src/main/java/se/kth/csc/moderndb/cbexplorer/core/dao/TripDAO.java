package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.core.domain.*;
import se.kth.csc.moderndb.cbexplorer.core.domain.params.TripParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.params.UserParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.TimeRange;
import se.kth.csc.moderndb.cbexplorer.domain.PSQLConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Jeannine on 09.05.14.
 */
public class TripDAO implements TripDAOi {

    private static final int BIKE_ID_COLUMN = 1;

    private final String START_RANGE = "_start";
    private final String END_RANGE = "_end";

    // for building dynamic sql queries
    private boolean alreadyAdded;

    private final JdbcTemplate jdbcTemplate;
    private final StationDAOi stationDAO;
    private final RowMapper<Trip> tripRowMapper;

    /**
     * Creates Access Object for the Trip table.
     *
     * @param source Data source
     * @param stationDAO Station DAO for accessing Stations.
     */
    public TripDAO(DataSource source, StationDAOi stationDAO) {
        if (source == null || stationDAO == null)
            throw new NullPointerException("Can't have null DataSource or StationDAOi");
        this.jdbcTemplate = new JdbcTemplate(source);
        this.stationDAO = stationDAO;
        this.tripRowMapper = new TripRowMapper(this.stationDAO);
    }

    @Override
    public List<Bike> findAllBikes() {
        String sql = "SELECT DISTINCT " + PSQLConnection.BIKEID + " FROM " + PSQLConnection.TRIP;
        return jdbcTemplate.query(sql, new Object[]{}, new BikeMapper());
    }

    @Override
    public List<Trip> findTripByID(long bikeID, Date startDate) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.BIKEID + " = ? AND "
                + PSQLConnection.STARTTIME + " = ?";
        return jdbcTemplate.query(sql, new Object[]{bikeID, startDate}, tripRowMapper);
    }

    /**
     * This method constructs and execute a query that can specify trips according to user data.
     * That means the gender, the age range and the user type can be used to select trips.
     *
     * @param userParameters above described attributes of the user making the trip
     * @return trip fitting the given attributes about the user
     */
    @Override
    public List<Trip> findTripSpecifiedByUserCharacteristics(UserParameters userParameters) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP + " WHERE";
        Connection conn = null;
        this.alreadyAdded = false;
        HashMap<String, Object> argument = addUserParametersToQuery(sql, userParameters);
        ArrayList<Object> args = new ArrayList<Object>();
        if (argument.containsKey(PSQLConnection.GENDER)) {
            int gender = ((Integer) argument.get(PSQLConnection.GENDER)).intValue();
            args.add(0, gender);
        }
        if (argument.containsKey(PSQLConnection.BIRTHYEAR + START_RANGE)) {
            int birthyearStart = ((Integer) argument.get(PSQLConnection.BIRTHYEAR + START_RANGE)).intValue();
            args.add(1, birthyearStart);
        }
        if (argument.containsKey(PSQLConnection.BIRTHYEAR + END_RANGE)) {
            int birthyearEnd = ((Integer) argument.get(PSQLConnection.BIRTHYEAR + END_RANGE)).intValue();
            args.add(2, birthyearEnd);
        }
        if (argument.containsKey(PSQLConnection.USERTYPE)) {
            String userType = ((String) argument.get(PSQLConnection.USERTYPE));
            args.add(4, userType);
        }
        // filter out all the arrays that are not null
        ArrayList<Object> notNullArgs = new ArrayList<Object>();
        for(Object arg : args){
            if(arg != null){
                notNullArgs.add(arg);
            }
        }

        return jdbcTemplate.query(sql, notNullArgs.toArray(), tripRowMapper);
    }

    /**
     * Finds trips that distances are between {@param tripParameters}.startOfTripDistanceRange and {@param tripParameters}.endOfTripDistanceRange and
     *
     * @param tripParameters give the distance range
     * @return trip with distance in given range
     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
     */
    @Override
    public List<Trip> findTripWithDistanceBetween(TripParameters tripParameters) throws IllegalArgumentException {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " +
                "(Select ST_Distance(JD.end_point,point)" +
                "from station,(Select point as end_point" +
                "from station" +
                "where " + PSQLConnection.TRIP + "." + PSQLConnection.STARTSTATION + " = station_id)JD" +
                "where " + PSQLConnection.TRIP + "." + PSQLConnection.ENDSTATION + " = station_id)";
        long start = tripParameters.getStartOfTripDistanceRange();
        long end = tripParameters.getEndOfTripDistanceRange();
        ArrayList<Object> args = new ArrayList<Object>();
        if (start >= 0) {
            if (end > 0) {
                if (start < end) {
                    sql.concat(" BETWEEN ? AND ?");
                    args.add(0, start);
                    args.add(1, end);
                } else {
                    if (start == end) {
                        sql.concat(" = ?");
                        args.add(0, start);
                    } else {
                        throw new IllegalArgumentException("End of distance range must be bigger than start");
                    }
                }
            } else {
                if (end == 0) {
                    sql.concat(" >= ?");
                    args.add(0, start);
                } else {
                    throw new IllegalArgumentException("End of distance range must at least be 0");
                }
            }
        } else {
            throw new IllegalArgumentException("End of distance range must at least be 0");
        }
        return jdbcTemplate.query(sql, args.toArray(), tripRowMapper);
    }

    /**
     * Finds trips that duration are between {@param tripParameters}.startOfTripDurationRange and {@param tripParameters}.endOfTripDurationRange and
     *
     * @param tripParameters give the duration range
     * @return trip with duration in given range
     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
     */
    @Override
    public List<Trip> findTripWithDurationBetween(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.DURATION;
        long start = tripParameters.getStartOfTripDurationRange();
        long end = tripParameters.getEndOfTripDurationRange();
        ArrayList<Object> args = new ArrayList<Object>();
        if (start >= 0) {
            if (end > 0) {
                if (start < end) {
                    sql.concat(" BETWEEN ? AND ?");
                    args.add(0, start);
                    args.add(1, end);
                } else {
                    if (start == end) {
                        sql.concat(" = ?");
                        args.add(0, start);
                    } else {
                        throw new IllegalArgumentException("End of duration range must be bigger than start");
                    }
                }
            } else {
                if (end == 0) {
                    sql.concat(" >= ?");
                    args.add(0, start);
                } else {
                    throw new IllegalArgumentException("End of duration range must at least be 0");
                }
            }
        } else {
            throw new IllegalArgumentException("End of duration range must at least be 0");
        }
        return jdbcTemplate.query(sql, args.toArray(), tripRowMapper);
    }

    @Override
    public List<Trip> findTripWithinTimeRange(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE";
        long start = tripParameters.getStartOfTripStartTimeRange();
        long end = tripParameters.getEndOfTripStartTimeRange();
        this.alreadyAdded = false;
        ArrayList<Object> args = new ArrayList<Object>();
        // conditions for start time
        if (start > 0) {
            sql.concat(" " + PSQLConnection.STARTTIME);
            this.alreadyAdded = true;
            if (end > 0) {
                if (start < end) {
                    sql.concat(" BETWEEN ? AND ?");
                    args.add(0, start);
                    args.add(1, end);
                } else {
                    if (start == end) {
                        sql.concat(" = ?");
                        args.add(0, start);
                    } else {
                        throw new IllegalArgumentException("End of start time range must be bigger than start");
                    }
                }
            } else {
                if (end == 0) {
                    sql.concat(" >= ?");
                    args.add(0, start);
                } else {
                    throw new IllegalArgumentException("End of start time range must at least be 0");
                }
            }
        } else {
            throw new IllegalArgumentException("End of start time range must at least be 0");
        }

        start = tripParameters.getStartOfTripStartTimeRange();
        end = tripParameters.getEndOfTripStartTimeRange();
        ArrayList<Object> argsEnd = new ArrayList<Object>();
        // conditions for end time
        if (start > 0) {
            if(this.alreadyAdded){
                sql.concat(" AND ");
            }
            sql.concat(" " + PSQLConnection.ENDTIME);
            this.alreadyAdded = true;
            if (end > 0) {
                if (start < end) {
                    sql.concat(" BETWEEN ? AND ?");
                    argsEnd.add(0, start);
                    argsEnd.add(1,end);
                } else {
                    if (start == end) {
                        sql.concat(" = ?");
                        argsEnd.add(0, start);
                    } else {
                        throw new IllegalArgumentException("End of end time range must be bigger than start");
                    }
                }
            } else {
                if (end == 0) {
                    sql.concat(" >= ?");
                    argsEnd.add(0, start);
                } else {
                    throw new IllegalArgumentException("End of end time range must at least be 0");
                }
            }
        } else {
            throw new IllegalArgumentException("End of duration range must at least be 0");
        }
        args.addAll(argsEnd);
        return jdbcTemplate.query(sql, args.toArray(), tripRowMapper);
    }

    @Override
    public List<Trip> findTripWithBikes(TripParameters tripParameters) throws IllegalArgumentException {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.BIKEID + " = ?";
        if (tripParameters.getBikeIDs() == null || tripParameters.getBikeIDs().size() == 0) {
            throw new IllegalArgumentException("No bike id(s) selected");
        }
        ArrayList<Long> bikeIDs = tripParameters.getBikeIDs();
        for (long bikeID : bikeIDs) {
            List<Trip> results = jdbcTemplate.query(sql, new Object[] {bikeID}, tripRowMapper);
            return results;
        }
        return null;
    }

    @Override
    public List<Trip> findTripWithStartStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.STARTSTATION + " = ?";

        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No start station(s) selected");
        }
        ArrayList<Station> stations = tripParameters.getStartStation();
        for (Station station : stations) {
            List<Trip> results = jdbcTemplate.query(sql, new Object[] {station}, tripRowMapper);
            return results;
        }
        return null;
    }

    @Override
    public List<Trip> findTripWithEndStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.ENDSTATION + " = ?";

        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No end station(s) selected");
        }
        ArrayList<Station> stations = tripParameters.getEndStation();
        for (Station station : stations) {
            return jdbcTemplate.query(sql, new Object[] {station}, tripRowMapper);
        }
        return null;
    }


    private HashMap<String, Object> addUserParametersToQuery(String sql, UserParameters userParameters) {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        char gender = userParameters.getGender();
        ShortRange birthRange = userParameters.getBirthYearRange();
        int ageStart = birthRange.getMin();
        int ageEnd = birthRange.getMax();
        String userType = userParameters.getUserType();
        if (gender == 'M' || gender == 'F') {
            sql.concat(" " + PSQLConnection.GENDER + " = ?");
            alreadyAdded = true;
            arguments.put(PSQLConnection.GENDER, gender);
        }
        if (ageStart > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int birthyearStartRange = year - ageStart;
            sql.concat(" " + PSQLConnection.BIRTHYEAR + " >= ?");
            arguments.put(PSQLConnection.BIRTHYEAR + START_RANGE, birthyearStartRange);
        }
        if (ageEnd > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int birthyearEndRange = year - ageEnd;
            sql.concat(" " + PSQLConnection.BIRTHYEAR + " <= ?");
            arguments.put(PSQLConnection.BIRTHYEAR + END_RANGE, birthyearEndRange);
        }
        if (userType != null) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            sql.concat(" " + PSQLConnection.USERTYPE + " = ?");
            arguments.put(PSQLConnection.USERTYPE, userType);
        }

        return arguments;
    }

    @Override
    public TimeRange getTripTimeLimits() {
        String sql = "SELECT MIN(" + PSQLConnection.STARTTIME + "), " +
                "MAX(" + PSQLConnection.ENDTIME + ") from "
                + PSQLConnection.TRIP;

        // There should only be one result.
        return jdbcTemplate.query(sql, new Object[]{}, new TimeRangeMapper()).get(0);
    }

    @Override
    public IntegerRange getTripDurationLimits() {
        String sql = "SELECT MIN(" + PSQLConnection.DURATION + "), " +
                "MAX(" + PSQLConnection.DURATION + ") from "
                + PSQLConnection.TRIP;
        return jdbcTemplate.query(sql, new Object[]{}, new IntegerRangeMapper()).get(0);
    }

    @Override
    public ShortRange getUserBirthYearLimits() {
        String sql = "SELECT MIN(" + PSQLConnection.BIRTHYEAR + "), " +
                "MAX(" + PSQLConnection.BIRTHYEAR + ") from "
                + PSQLConnection.TRIP;
        return jdbcTemplate.query(sql, new Object[]{}, new ShortRangeMapper()).get(0);
    }

    @Override
    public List<String> getUserTypes() {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Row Mappers
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class ShortRangeMapper implements RowMapper<ShortRange> {
        @Override
        public ShortRange mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ShortRange(rs.getShort(1), rs.getShort(2));
        }
    }

    private static class IntegerRangeMapper implements RowMapper<IntegerRange> {
        @Override
        public IntegerRange mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new IntegerRange(rs.getInt(1), rs.getInt(2));
        }
    }

    private static class TimeRangeMapper implements RowMapper<TimeRange> {
        @Override
        public TimeRange mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeRange(new Date(rs.getDate(1).getTime()), new Date(rs.getDate(2).getTime()));
        }
    }

    private static class TripRowMapper implements RowMapper<Trip> {

        private final StationDAOi stationDAO;

        TripRowMapper(StationDAOi stationDAO) {
            this.stationDAO = stationDAO;
        }

        @Override
        public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bike bike = new Bike(rs.getLong(PSQLConnection.BIKEID));
            Station startStation = null;
            Station endStation = null;
            try {
                startStation = stationDAO.findStationByID(rs.getLong(PSQLConnection.STARTSTATION)).get(0);
                endStation = stationDAO.findStationByID(rs.getLong(PSQLConnection.ENDSTATION)).get(0);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return new Trip(
                    new Date (rs.getDate(PSQLConnection.STARTTIME).getTime()),
                    new Date (rs.getDate(PSQLConnection.ENDTIME).getTime()),
                    rs.getInt(PSQLConnection.DURATION),
                    rs.getString(PSQLConnection.USERTYPE),
                    rs.getShort(PSQLConnection.BIRTHYEAR),
                    rs.getShort(PSQLConnection.GENDER),
                    startStation,
                    endStation,
                    bike);
        }
    }

    /**
     * Mapping class that maps a single bike id to a bike class.
     */
    private static class BikeMapper implements RowMapper<Bike> {
        @Override
        public Bike mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Bike(rs.getLong(BIKE_ID_COLUMN));
        }
    }

}
