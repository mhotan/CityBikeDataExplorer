package se.kth.csc.moderndb.cbexplorer.core.st_queries;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import se.kth.csc.moderndb.cbexplorer.core.dataAccessObject.TripDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.*;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Jeannine on 09.05.14.
 */
public class TripDAO implements TripDAOi {

    private final String START_RANGE = "_start";
    private final String END_RANGE = "_end";

    // for building dynamic sql queries
    private boolean alreadyAdded;

    private JdbcTemplate jdbcTemplate;


    public TripDAO() {
        SimpleDriverDataSource driverDataSource = new SimpleDriverDataSource() {{
            setDriverClass(org.postgresql.Driver.class);
            setUsername(PostgreSQLDatabaseConnection.USERNAME);
            setUrl(PostgreSQLDatabaseConnection.URL + PostgreSQLDatabaseConnection.DATABASE_NAME);
            setPassword(PostgreSQLDatabaseConnection.PASSWORD);
        }};
        this.jdbcTemplate = new JdbcTemplate(driverDataSource);


    }


    @Override
    public List<Trip> findTripByID(long bikeID, Date startDate) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.BIKEID + " = ? AND " + PostgreSQLDatabaseConnection.STARTTIME + " = ?";
        List<Trip> results = jdbcTemplate.query(
                sql, new Object[]{bikeID, startDate},
                new RowMapper<Trip>() {
                    @Override
                    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                        StationDAO stationDAO = new StationDAO();
                        Station startStation = null;
                        Station endStation = null;
                        try {
                            startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                            endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return new Trip(
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                startStation,
                                endStation,
                                bike);
                    }});
    return results;
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
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP + " WHERE";
        Connection conn = null;
        this.alreadyAdded = false;
        HashMap<String, Object> argument = addUserParametersToQuery(sql, userParameters);
        ArrayList<Object> args = new ArrayList<Object>();
        if (argument.containsKey(PostgreSQLDatabaseConnection.GENDER)) {
                int gender = ((Integer) argument.get(PostgreSQLDatabaseConnection.GENDER)).intValue();
                args.add(0, gender);
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.BIRTHYEAR + START_RANGE)) {
                int birthyearStart = ((Integer) argument.get(PostgreSQLDatabaseConnection.BIRTHYEAR + START_RANGE)).intValue();
                args.add(1, birthyearStart);
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.BIRTHYEAR + END_RANGE)) {
                int birthyearEnd = ((Integer) argument.get(PostgreSQLDatabaseConnection.BIRTHYEAR + END_RANGE)).intValue();
               args.add(2, birthyearEnd);
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.USERTYPE)) {
                String userType = ((String) argument.get(PostgreSQLDatabaseConnection.USERTYPE));
                args.add(4, userType);
            }
        // filter out all the arrays that are not null
        ArrayList<Object> notNullArgs = new ArrayList<Object>();
        for(Object arg : args){
            if(arg != null){
                notNullArgs.add(arg);
            }
        }

        List<Trip> results = jdbcTemplate.query(
                sql, notNullArgs.toArray(),
                new RowMapper<Trip>() {
                    @Override
                    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                        StationDAO stationDAO = new StationDAO();
                        Station startStation = null;
                        Station endStation = null;
                        try {
                            startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                            endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return new Trip(
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                startStation,
                                endStation,
                                bike);
                    }});
        return results;
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
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " +
                "(Select ST_Distance(JD.end_point,point)" +
                "from station,(Select point as end_point" +
                "from station" +
                "where " + PostgreSQLDatabaseConnection.TRIP + "." + PostgreSQLDatabaseConnection.STARTSTATION + " = station_id)JD" +
                "where " + PostgreSQLDatabaseConnection.TRIP + "." + PostgreSQLDatabaseConnection.ENDSTATION + " = station_id)";
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
        List<Trip> results = jdbcTemplate.query(
                sql, args.toArray(),
                new RowMapper<Trip>() {
                    @Override
                    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                        StationDAO stationDAO = new StationDAO();
                        Station startStation = null;
                        Station endStation = null;
                        try {
                            startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                            endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return new Trip(
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                startStation,
                                endStation,
                                bike);
                    }});
        return results;
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
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.DURATION;
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
        List<Trip> results = jdbcTemplate.query(
                sql, args.toArray(),
                new RowMapper<Trip>() {
                    @Override
                    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                        StationDAO stationDAO = new StationDAO();
                        Station startStation = null;
                        Station endStation = null;
                        try {
                            startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                            endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return new Trip(
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                startStation,
                                endStation,
                                bike);
                    }});
        return results;
    }

    @Override
    public List<Trip> findTripWithinTimeRange(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE";
            long start = tripParameters.getStartOfTripStartTimeRange();
            long end = tripParameters.getEndOfTripStartTimeRange();
            this.alreadyAdded = false;
        ArrayList<Object> args = new ArrayList<Object>();
            // conditions for start time
            if (start > 0) {
                sql.concat(" " + PostgreSQLDatabaseConnection.STARTTIME);
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
                sql.concat(" " + PostgreSQLDatabaseConnection.ENDTIME);
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
        List<Trip> results = jdbcTemplate.query(
                sql, args.toArray(),
                new RowMapper<Trip>() {
                    @Override
                    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                        StationDAO stationDAO = new StationDAO();
                        Station startStation = null;
                        Station endStation = null;
                        try {
                            startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                            endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return new Trip(
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                startStation,
                                endStation,
                                bike);
                    }});
        return results;
    }

    @Override
    public List<Trip> findTripWithBikes(TripParameters tripParameters) throws IllegalArgumentException {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.BIKEID + " = ?";
        if (tripParameters.getBikeIDs() == null || tripParameters.getBikeIDs().size() == 0) {
            throw new IllegalArgumentException("No bike id(s) selected");
        }
            ArrayList<Long> bikeIDs = tripParameters.getBikeIDs();
            for (long bikeID : bikeIDs) {
                List<Trip> results = jdbcTemplate.query(
                        sql, new Object[] {bikeID},
                        new RowMapper<Trip>() {
                            @Override
                            public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                                StationDAO stationDAO = new StationDAO();
                                Station startStation = null;
                                Station endStation = null;
                                try {
                                    startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                                    endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                return new Trip(
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                        rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                        startStation,
                                        endStation,
                                        bike);
                            }});
                return results;
            }
        return null;
    }

    @Override
    public List<Trip> findTripWithStartStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.STARTSTATION + " = ?";

        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No start station(s) selected");
        }
            ArrayList<Station> stations = tripParameters.getStartStation();
            for (Station station : stations) {
                List<Trip> results = jdbcTemplate.query(
                        sql, new Object[] {station},
                        new RowMapper<Trip>() {
                            @Override
                            public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                                StationDAO stationDAO = new StationDAO();
                                Station startStation = null;
                                Station endStation = null;
                                try {
                                    startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                                    endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                return new Trip(
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                        rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                        startStation,
                                        endStation,
                                        bike);
                            }});
                return results;
            }
        return null;
    }

    @Override
    public List<Trip> findTripWithEndStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.ENDSTATION + " = ?";

        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No end station(s) selected");
        }
            ArrayList<Station> stations = tripParameters.getEndStation();
            for (Station station : stations) {
                List<Trip> results = jdbcTemplate.query(
                        sql, new Object[] {station},
                        new RowMapper<Trip>() {
                            @Override
                            public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                                StationDAO stationDAO = new StationDAO();
                                Station startStation = null;
                                Station endStation = null;
                                try {
                                    startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0);
                                    endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                return new Trip(
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                                        rs.getInt(PostgreSQLDatabaseConnection.DURATION),
                                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                                        startStation,
                                        endStation,
                                        bike);
                            }});
                return results;
            }
        return null;
    }


    private HashMap<String, Object> addUserParametersToQuery(String sql, UserParameters userParameters) {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        char gender = userParameters.getGender();
        int ageStart = userParameters.getStartRangeAge();
        int ageEnd = userParameters.getEndRangeAge();
        String userType = userParameters.getUserType();
        if (gender == new Character('M') || gender == new Character('F')) {
            sql.concat(" " + PostgreSQLDatabaseConnection.GENDER + " = ?");
            alreadyAdded = true;
            arguments.put(PostgreSQLDatabaseConnection.GENDER, gender);
        }
        if (ageStart > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int birthyearStartRange = year - ageStart;
            sql.concat(" " + PostgreSQLDatabaseConnection.BIRTHYEAR + " >= ?");
            arguments.put(PostgreSQLDatabaseConnection.BIRTHYEAR + START_RANGE, birthyearStartRange);
        }
        if (ageEnd > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int birthyearEndRange = year - ageEnd;
            sql.concat(" " + PostgreSQLDatabaseConnection.BIRTHYEAR + " <= ?");
            arguments.put(PostgreSQLDatabaseConnection.BIRTHYEAR + END_RANGE, birthyearEndRange);
        }
        if (userType != null) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            sql.concat(" " + PostgreSQLDatabaseConnection.USERTYPE + " = ?");
            arguments.put(PostgreSQLDatabaseConnection.USERTYPE, userType);
        }

        return arguments;
    }

}
