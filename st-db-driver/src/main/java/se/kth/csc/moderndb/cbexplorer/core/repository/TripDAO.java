package se.kth.csc.moderndb.cbexplorer.core.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.PSQLConnection;
import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;
import se.kth.csc.moderndb.cbexplorer.core.params.BikeParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.TemporalParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.UserParameters;
import se.kth.csc.moderndb.cbexplorer.core.range.IntegerRange;
import se.kth.csc.moderndb.cbexplorer.core.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.range.TimeRange;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeannine on 09.05.14.
 */
public class TripDAO implements TripDAOi {

    // Cache Specifications
    private static final int CACHE_DEFAULT_EXPIRATION_TIME_MIN = 10; // In minutes
    private static final int CACHE_DEFAULT_SIZE = 10000; // Total number of trips

    // Bike Queries
    private static final int BIKE_ID_COLUMN = 1;
    private static final String BASE_BIKE_QUERY = "SELECT DISTINCT "
            + PSQLConnection.BIKE_ID + " FROM " + PSQLConnection.TRIP;

    private final String START_RANGE = "_start";
    private final String END_RANGE = "_end";



    // for building dynamic sql queries
    private boolean alreadyAdded;

    private final JdbcTemplate jdbcTemplate;
    private final StationDAOi stationDAO;

    private final TripStore tripStore;
    private final BikeStore bikeStore;

    /**
     * Creates Access Object for the Trip table.
     *
     * @param source Data source that connect PSQL.
     * @param stationDAO Station DAO for accessing Stations.
     */
    @Autowired
    public TripDAO(DataSource source, StationDAOi stationDAO) {
        if (source == null || stationDAO == null)
            throw new NullPointerException("Can't have null DataSource or StationDAOi");
        this.jdbcTemplate = new JdbcTemplate(source);
        this.stationDAO = stationDAO;
        this.tripStore = new TripStore(this.stationDAO);
        this.bikeStore = BikeStore.getInstance();
    }

    @Override
    public Bike findBikeByID(long bikeID) {
        // Check the cache first.
        String sql = BASE_BIKE_QUERY + " WHERE " + PSQLConnection.BIKE_ID + " = ?";
        List<Bike> bikes = jdbcTemplate.query(sql.trim(), new Object[]{bikeID}, this.bikeStore);
        if (bikes.isEmpty()) return null;
        return bikes.get(0);
    }

    @Override
    public List<Bike> findAllBikes() {
        return jdbcTemplate.query(BASE_BIKE_QUERY.trim(), new Object[]{}, this.bikeStore);
    }

    @Override
    public Trip findTripByID(long bikeID, Date startDate) {
        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
                " WHERE " + PSQLConnection.BIKE_ID + " = ? AND "
                + PSQLConnection.START_TIME + " = ?";
        List<Trip> trips = jdbcTemplate.query(sql, new Object[]{bikeID, startDate}, tripStore);
        if (trips.size() > 1) {
            throw new RuntimeException("Multiple trips with id Bike ID: "
                    + bikeID + " Start DateTime " + startDate.toString());
        }
        if (trips.isEmpty()) return null;
        return trips.get(0);
    }

    @Override
    public List<Trip> findTrips(Long startStation, Long endStation,
                                BikeParameters bikeParameters, UserParameters userParameters,
                                TemporalParameters temporalParameters) {
        // todo
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
            sql.concat(" " + PSQLConnection.BIRTH_YEAR + " >= ?");
            arguments.put(PSQLConnection.BIRTH_YEAR + START_RANGE, birthyearStartRange);
        }
        if (ageEnd > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int birthyearEndRange = year - ageEnd;
            sql.concat(" " + PSQLConnection.BIRTH_YEAR + " <= ?");
            arguments.put(PSQLConnection.BIRTH_YEAR + END_RANGE, birthyearEndRange);
        }
        if (userType != null) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            sql.concat(" " + PSQLConnection.USER_TYPE + " = ?");
            arguments.put(PSQLConnection.USER_TYPE, userType);
        }

        return arguments;
    }

    @Override
    public TimeRange getTripTimeLimits() {
        String sql = "SELECT MIN(" + PSQLConnection.START_TIME + "), " +
                "MAX(" + PSQLConnection.END_TIME + ") from "
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
        String sql = "SELECT MIN(" + PSQLConnection.BIRTH_YEAR + "), " +
                "MAX(" + PSQLConnection.BIRTH_YEAR + ") from "
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

        private static TimeRangeMapper instance;

        private TimeRangeMapper() {} // cannot instantiate

        static TimeRangeMapper getInstance() {
            if (instance == null)
                instance = new TimeRangeMapper();
            return instance;
        }

        @Override
        public TimeRange mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeRange(new Date(rs.getDate(1).getTime()), new Date(rs.getDate(2).getTime()));
        }
    }

    /**
     * Data store for trips.
     */
    private static class TripStore implements RowMapper<Trip> {

        /**
         * The Data access object for obtaining stations
         */
        private final StationDAOi stationDAO;

        /**
         * Cache for all the remaining trips.
         */
        private final Cache<TripKey, Trip> tripCache;

        TripStore(StationDAOi stationDAO) {
            this.stationDAO = stationDAO;
            this.tripCache = CacheBuilder.newBuilder()
                    .maximumSize(CACHE_DEFAULT_SIZE)
                    .expireAfterAccess(CACHE_DEFAULT_EXPIRATION_TIME_MIN, TimeUnit.MINUTES)
                    .build();
        }

        @Override
        public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Check if we have the trip in the cache.
            Bike bike = new Bike(rs.getLong(PSQLConnection.BIKE_ID));
            Date startTime = new Date (rs.getDate(PSQLConnection.START_TIME).getTime());
            TripKey key = new TripKey(bike, startTime);
            Trip trip = tripCache.getIfPresent(key);
            if (trip != null) {
                return trip;
            }

            Station startStation = null;
            Station endStation = null;
            try {
                startStation = stationDAO.findStationByID(rs.getLong(PSQLConnection.START_STATION)).get(0);
                endStation = stationDAO.findStationByID(rs.getLong(PSQLConnection.END_STATION)).get(0);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            trip = new Trip(
                    startTime,
                    new Date (rs.getDate(PSQLConnection.END_TIME).getTime()),
                    rs.getInt(PSQLConnection.DURATION),
                    rs.getString(PSQLConnection.USER_TYPE),
                    rs.getShort(PSQLConnection.BIRTH_YEAR),
                    rs.getShort(PSQLConnection.GENDER),
                    startStation,
                    endStation,
                    bike);
            tripCache.put(key, trip);
            return trip;
        }
    }

    /**
     * Mapping class that maps a single bike id to a bike class.
     */
    private static class BikeStore implements RowMapper<Bike> {

        private static BikeStore instance;

        static BikeStore getInstance() {
            if (instance == null)
                instance = new BikeStore();
            return instance;
        }

        private final Cache<Long, Bike> bikeCache;

        private BikeStore() {
            bikeCache = CacheBuilder.newBuilder()
                    .maximumSize(CACHE_DEFAULT_SIZE)
                    .expireAfterAccess(CACHE_DEFAULT_EXPIRATION_TIME_MIN, TimeUnit.MINUTES)
                    .build();
        }

        @Override
        public Bike mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Check if we have the Bike in the Cache.
            Long bikeId = rs.getLong(BIKE_ID_COLUMN);
            // Extract other fields
            Bike bike = bikeCache.getIfPresent(bikeId);
            if (bike != null) {
                // Update the Cache values.

                return bike;
            }

            bike = new Bike(bikeId);
            bikeCache.put(bikeId, bike);
            return bike;
        }
    }

    private static class TripKey {

        private final Bike bike;

        private final Date startTime;

        private TripKey(Bike bike, Date startTime) {
            this.bike = bike;
            this.startTime = startTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TripKey)) return false;

            TripKey tripKey = (TripKey) o;
            if (!bike.equals(tripKey.bike)) return false;
            if (!startTime.equals(tripKey.startTime)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = bike.hashCode();
            result = 31 * result + startTime.hashCode();
            return result;
        }
    }









    //    /**
//     * This method constructs and execute a query that can specify trips according to user data.
//     * That means the gender, the age range and the user type can be used to select trips.
//     *
//     * @param userParameters above described attributes of the user making the trip
//     * @return trip fitting the given attributes about the user
//     */
//    @Override
//    public List<Trip> findTripSpecifiedByUserCharacteristics(UserParameters userParameters) {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP + " WHERE";
//        Connection conn = null;
//        this.alreadyAdded = false;
//        HashMap<String, Object> argument = addUserParametersToQuery(sql, userParameters);
//        ArrayList<Object> args = new ArrayList<Object>();
//        if (argument.containsKey(PSQLConnection.GENDER)) {
//            int gender = ((Integer) argument.get(PSQLConnection.GENDER)).intValue();
//            args.add(0, gender);
//        }
//        if (argument.containsKey(PSQLConnection.BIRTH_YEAR + START_RANGE)) {
//            int birthyearStart = ((Integer) argument.get(PSQLConnection.BIRTH_YEAR + START_RANGE)).intValue();
//            args.add(1, birthyearStart);
//        }
//        if (argument.containsKey(PSQLConnection.BIRTH_YEAR + END_RANGE)) {
//            int birthyearEnd = ((Integer) argument.get(PSQLConnection.BIRTH_YEAR + END_RANGE)).intValue();
//            args.add(2, birthyearEnd);
//        }
//        if (argument.containsKey(PSQLConnection.USER_TYPE)) {
//            String userType = ((String) argument.get(PSQLConnection.USER_TYPE));
//            args.add(4, userType);
//        }
//        // filter out all the arrays that are not null
//        ArrayList<Object> notNullArgs = new ArrayList<Object>();
//        for(Object arg : args){
//            if(arg != null){
//                notNullArgs.add(arg);
//            }
//        }
//
//        return jdbcTemplate.query(sql, notNullArgs.toArray(), tripStore);
//    }

//    /**
//     * Finds trips that distances are between {@param tripParameters}.startOfTripDistanceRange and {@param tripParameters}.endOfTripDistanceRange and
//     *
//     * @param tripParameters give the distance range
//     * @return trip with distance in given range
//     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
//     */
//    @Override
//    public List<Trip> findTripWithDistanceBetween(TripParameters tripParameters) throws IllegalArgumentException {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE " +
//                "(Select ST_Distance(JD.end_point,point)" +
//                "from station,(Select point as end_point" +
//                "from station" +
//                "where " + PSQLConnection.TRIP + "." + PSQLConnection.START_STATION + " = station_id)JD" +
//                "where " + PSQLConnection.TRIP + "." + PSQLConnection.END_STATION + " = station_id)";
//        long start = tripParameters.getStartOfTripDistanceRange();
//        long end = tripParameters.getEndOfTripDistanceRange();
//        ArrayList<Object> args = new ArrayList<Object>();
//        if (start >= 0) {
//            if (end > 0) {
//                if (start < end) {
//                    sql.concat(" BETWEEN ? AND ?");
//                    args.add(0, start);
//                    args.add(1, end);
//                } else {
//                    if (start == end) {
//                        sql.concat(" = ?");
//                        args.add(0, start);
//                    } else {
//                        throw new IllegalArgumentException("End of distance range must be bigger than start");
//                    }
//                }
//            } else {
//                if (end == 0) {
//                    sql.concat(" >= ?");
//                    args.add(0, start);
//                } else {
//                    throw new IllegalArgumentException("End of distance range must at least be 0");
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("End of distance range must at least be 0");
//        }
//        return jdbcTemplate.query(sql, args.toArray(), tripStore);
//    }

//    /**
//     * Finds trips that duration are between {@param tripParameters}.startOfTripDurationRange and {@param tripParameters}.endOfTripDurationRange and
//     *
//     * @param tripParameters give the duration range
//     * @return trip with duration in given range
//     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
//     */
//    @Override
//    public List<Trip> findTripWithDurationBetween(TripParameters tripParameters) {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.DURATION;
//        long start = tripParameters.getStartOfTripDurationRange();
//        long end = tripParameters.getEndOfTripDurationRange();
//        ArrayList<Object> args = new ArrayList<Object>();
//        if (start >= 0) {
//            if (end > 0) {
//                if (start < end) {
//                    sql.concat(" BETWEEN ? AND ?");
//                    args.add(0, start);
//                    args.add(1, end);
//                } else {
//                    if (start == end) {
//                        sql.concat(" = ?");
//                        args.add(0, start);
//                    } else {
//                        throw new IllegalArgumentException("End of duration range must be bigger than start");
//                    }
//                }
//            } else {
//                if (end == 0) {
//                    sql.concat(" >= ?");
//                    args.add(0, start);
//                } else {
//                    throw new IllegalArgumentException("End of duration range must at least be 0");
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("End of duration range must at least be 0");
//        }
//        return jdbcTemplate.query(sql, args.toArray(), tripStore);
//    }
//
//    @Override
//    public List<Trip> findTripWithinTimeRange(TripParameters tripParameters) {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE";
//        long start = tripParameters.getStartOfTripStartTimeRange();
//        long end = tripParameters.getEndOfTripStartTimeRange();
//        this.alreadyAdded = false;
//        ArrayList<Object> args = new ArrayList<Object>();
//        // conditions for start time
//        if (start > 0) {
//            sql.concat(" " + PSQLConnection.START_TIME);
//            this.alreadyAdded = true;
//            if (end > 0) {
//                if (start < end) {
//                    sql.concat(" BETWEEN ? AND ?");
//                    args.add(0, start);
//                    args.add(1, end);
//                } else {
//                    if (start == end) {
//                        sql.concat(" = ?");
//                        args.add(0, start);
//                    } else {
//                        throw new IllegalArgumentException("End of start time range must be bigger than start");
//                    }
//                }
//            } else {
//                if (end == 0) {
//                    sql.concat(" >= ?");
//                    args.add(0, start);
//                } else {
//                    throw new IllegalArgumentException("End of start time range must at least be 0");
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("End of start time range must at least be 0");
//        }
//
//        start = tripParameters.getStartOfTripStartTimeRange();
//        end = tripParameters.getEndOfTripStartTimeRange();
//        ArrayList<Object> argsEnd = new ArrayList<Object>();
//        // conditions for end time
//        if (start > 0) {
//            if(this.alreadyAdded){
//                sql.concat(" AND ");
//            }
//            sql.concat(" " + PSQLConnection.END_TIME);
//            this.alreadyAdded = true;
//            if (end > 0) {
//                if (start < end) {
//                    sql.concat(" BETWEEN ? AND ?");
//                    argsEnd.add(0, start);
//                    argsEnd.add(1,end);
//                } else {
//                    if (start == end) {
//                        sql.concat(" = ?");
//                        argsEnd.add(0, start);
//                    } else {
//                        throw new IllegalArgumentException("End of end time range must be bigger than start");
//                    }
//                }
//            } else {
//                if (end == 0) {
//                    sql.concat(" >= ?");
//                    argsEnd.add(0, start);
//                } else {
//                    throw new IllegalArgumentException("End of end time range must at least be 0");
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("End of duration range must at least be 0");
//        }
//        args.addAll(argsEnd);
//        return jdbcTemplate.query(sql, args.toArray(), tripStore);
//    }
//
//    @Override
//    public List<Trip> findTripWithBikes(TripParameters tripParameters) throws IllegalArgumentException {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.BIKE_ID + " = ?";
//        if (tripParameters.getBikeIDs() == null || tripParameters.getBikeIDs().size() == 0) {
//            throw new IllegalArgumentException("No bike id(s) selected");
//        }
//        ArrayList<Long> bikeIDs = tripParameters.getBikeIDs();
//        for (long bikeID : bikeIDs) {
//            List<Trip> results = jdbcTemplate.query(sql, new Object[] {bikeID}, tripStore);
//            return results;
//        }
//        return null;
//    }
//
//    @Override
//    public List<Trip> findTripWithStartStations(long stationId) {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.START_STATION + " = ?";
//        return jdbcTemplate.query(sql, new Object[] {stationId}, tripStore);
//    }
//
//    @Override
//    public List<Trip> findTripWithEndStations(long stationId) {
//        String sql = "SELECT * FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.END_STATION + " = ?";
//        return jdbcTemplate.query(sql, new Object[] {stationId}, tripStore);
//    }


//    @Override
//    public Integer countTripDeparting(long stationId) {
//        String sql = "SELECT COUNT(*) FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.START_STATION + " = ?";
//        return jdbcTemplate.query(sql, new Object[] {stationId}, new RowMapper<Integer>() {
//            @Override
//            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return rs.getInt(1);
//            }
//        }).get(0);
//    }
//
//    @Override
//    public Integer countTripArriving(long stationId) {
//        String sql = "SELECT COUNT(*) FROM " + PSQLConnection.TRIP +
//                " WHERE " + PSQLConnection.END_STATION + " = ?";
//        return jdbcTemplate.query(sql, new Object[] {stationId}, new RowMapper<Integer>() {
//            @Override
//            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return rs.getInt(1);
//            }
//        }).get(0);
//    }
}
