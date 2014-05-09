package se.kth.csc.moderndb.cbexplorer.core.st_queries;

import se.kth.csc.moderndb.cbexplorer.core.dataAccessObject.TripDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.*;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jeannine on 09.05.14.
 */
public class TripDAO implements TripDAOi {

    private final String START_RANGE = "_start";
    private final String END_RANGE = "_end";


    private DataSource dataSource;
    // for building dynamic sql queries
    private boolean alreadyAdded;

    public TripDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Trip findTripByID(long bikeID, Date startDate) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.BIKEID + " = ? AND " + PostgreSQLDatabaseConnection.STARTTIME + " = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, bikeID);
            ps.setDate(2, new java.sql.Date(startDate.getTime()));
            Trip trip = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //String userType, short userBirthYear, short userGender, Station startedFrom, Station endedAt, Bike bike) {
                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                StationDAO stationDAO = new StationDAO(this.dataSource);
                Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                trip = new Trip(
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                        startStation,
                        endStation,
                        bike);
            }
            rs.close();
            ps.close();
            return trip;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * This method constructs and execute a query that can specify trips according to user data.
     * That means the gender, the age range and the user type can be used to select trips.
     *
     * @param userParameters above described attributes of the user making the trip
     * @return trip fitting the given attributes about the user
     */
    @Override
    public Trip findTripSpecifiedByUserCharacteristics(UserParameters userParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP + " WHERE";
        Connection conn = null;
        this.alreadyAdded = false;
        HashMap<String, Object> argument = addUserParametersToQuery(sql, userParameters);
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            int c = 1;
            if (argument.containsKey(PostgreSQLDatabaseConnection.GENDER)) {
                int gender = ((Integer) argument.get(PostgreSQLDatabaseConnection.GENDER)).intValue();
                ps.setInt(c, gender);
                c++;
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.BIRTHYEAR + START_RANGE)) {
                int birthyearStart = ((Integer) argument.get(PostgreSQLDatabaseConnection.BIRTHYEAR + START_RANGE)).intValue();
                ps.setInt(c, birthyearStart);
                c++;
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.BIRTHYEAR + END_RANGE)) {
                int birthyearEnd = ((Integer) argument.get(PostgreSQLDatabaseConnection.BIRTHYEAR + END_RANGE)).intValue();
                ps.setInt(c, birthyearEnd);
                c++;
            }
            if (argument.containsKey(PostgreSQLDatabaseConnection.USERTYPE)) {
                String userType = ((String) argument.get(PostgreSQLDatabaseConnection.USERTYPE));
                ps.setString(c, userType);
                c++;
            }
            Trip trip = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //String userType, short userBirthYear, short userGender, Station startedFrom, Station endedAt, Bike bike) {
                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                StationDAO stationDAO = new StationDAO(this.dataSource);
                Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                trip = new Trip(
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                        startStation,
                        endStation,
                        bike);
            }
            rs.close();
            ps.close();
            return trip;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * Finds trips that distances are between {@param tripParameters}.startOfTripDistanceRange and {@param tripParameters}.endOfTripDistanceRange and
     *
     * @param tripParameters give the distance range
     * @return trip with distance in given range
     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
     */
    @Override
    public Trip findTripWithDistanceBetween(TripParameters tripParameters) throws IllegalArgumentException {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " +
                "(Select ST_Distance(JD.end_point,point)" +
                "from station,(Select point as end_point" +
                "from station" +
                "where " + PostgreSQLDatabaseConnection.TRIP + "." + PostgreSQLDatabaseConnection.STARTSTATION + " = station_id)JD" +
                "where " + PostgreSQLDatabaseConnection.TRIP + "." + PostgreSQLDatabaseConnection.ENDSTATION + " = station_id)";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps;
            long start = tripParameters.getStartOfTripDistanceRange();
            long end = tripParameters.getEndOfTripDistanceRange();
            if (start >= 0) {
                if (end > 0) {
                    if (start < end) {
                        sql.concat(" BETWEEN ? AND ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                        ps.setLong(2, end);
                    } else {
                        if (start == end) {
                            sql.concat(" = ?");
                            ps = conn.prepareStatement(sql);
                            ps.setLong(1, start);
                        } else {
                            throw new IllegalArgumentException("End of distance range must be bigger than start");
                        }
                    }
                } else {
                    if (end == 0) {
                        sql.concat(" >= ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                    } else {
                        throw new IllegalArgumentException("End of distance range must at least be 0");
                    }
                }
            } else {
                throw new IllegalArgumentException("End of distance range must at least be 0");
            }
            Trip trip = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                StationDAO stationDAO = new StationDAO(this.dataSource);
                Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                trip = new Trip(
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                        startStation,
                        endStation,
                        bike);
            }
            rs.close();
            ps.close();
            return trip;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * Finds trips that duration are between {@param tripParameters}.startOfTripDurationRange and {@param tripParameters}.endOfTripDurationRange and
     *
     * @param tripParameters give the duration range
     * @return trip with duration in given range
     * @throws IllegalArgumentException if the start of the range is bigger than the end, the start or the end are smaller than 0
     */
    @Override
    public Trip findTripWithDurationBetween(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.DURATION;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps;
            long start = tripParameters.getStartOfTripDurationRange();
            long end = tripParameters.getEndOfTripDurationRange();
            if (start >= 0) {
                if (end > 0) {
                    if (start < end) {
                        sql.concat(" BETWEEN ? AND ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                        ps.setLong(2, end);
                    } else {
                        if (start == end) {
                            sql.concat(" = ?");
                            ps = conn.prepareStatement(sql);
                            ps.setLong(1, start);
                        } else {
                            throw new IllegalArgumentException("End of duration range must be bigger than start");
                        }
                    }
                } else {
                    if (end == 0) {
                        sql.concat(" >= ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                    } else {
                        throw new IllegalArgumentException("End of duration range must at least be 0");
                    }
                }
            } else {
                throw new IllegalArgumentException("End of duration range must at least be 0");
            }
            Trip trip = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                StationDAO stationDAO = new StationDAO(this.dataSource);
                Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                trip = new Trip(
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                        startStation,
                        endStation,
                        bike);
            }
            rs.close();
            ps.close();
            return trip;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public Trip findTripWithinTimeRange(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps;
            long start = tripParameters.getStartOfTripStartTimeRange();
            long end = tripParameters.getEndOfTripStartTimeRange();
            this.alreadyAdded = false;
            // conditions for start time
            if (start > 0) {
                sql.concat(" " + PostgreSQLDatabaseConnection.STARTTIME);
                this.alreadyAdded = true;
                if (end > 0) {
                    if (start < end) {
                        sql.concat(" BETWEEN ? AND ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                        ps.setLong(2, end);
                    } else {
                        if (start == end) {
                            sql.concat(" = ?");
                            ps = conn.prepareStatement(sql);
                            ps.setLong(1, start);
                        } else {
                            throw new IllegalArgumentException("End of start time range must be bigger than start");
                        }
                    }
                } else {
                    if (end == 0) {
                        sql.concat(" >= ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                    } else {
                        throw new IllegalArgumentException("End of start time range must at least be 0");
                    }
                }
            } else {
                throw new IllegalArgumentException("End of start time range must at least be 0");
            }

            start = tripParameters.getStartOfTripStartTimeRange();
            end = tripParameters.getEndOfTripStartTimeRange();
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
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                        ps.setLong(2, end);
                    } else {
                        if (start == end) {
                            sql.concat(" = ?");
                            ps = conn.prepareStatement(sql);
                            ps.setLong(1, start);
                        } else {
                            throw new IllegalArgumentException("End of end time range must be bigger than start");
                        }
                    }
                } else {
                    if (end == 0) {
                        sql.concat(" >= ?");
                        ps = conn.prepareStatement(sql);
                        ps.setLong(1, start);
                    } else {
                        throw new IllegalArgumentException("End of end time range must at least be 0");
                    }
                }
            } else {
                throw new IllegalArgumentException("End of duration range must at least be 0");
            }
            Trip trip = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                StationDAO stationDAO = new StationDAO(this.dataSource);
                Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                trip = new Trip(
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                        new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                        rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                        rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                        rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                        startStation,
                        endStation,
                        bike);
            }
            rs.close();
            ps.close();
            return trip;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public Trip findTripWithBikes(TripParameters tripParameters) throws IllegalArgumentException {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.BIKEID + " = ?";
        Connection conn = null;
        if (tripParameters.getBikeIDs() == null || tripParameters.getBikeIDs().size() == 0) {
            throw new IllegalArgumentException("No bike id(s) selected");
        }
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            Trip trip = null;
            ArrayList<Long> bikeIDs = tripParameters.getBikeIDs();
            for (long bikeID : bikeIDs) {
                ps.setLong(1, bikeID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                    StationDAO stationDAO = new StationDAO(this.dataSource);
                    Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                    Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                    trip = new Trip(
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                            rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                            rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                            rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                            startStation,
                            endStation,
                            bike);
                    rs.close();
                    ps.close(); // TODO check whether this is working or whether it must out of the for-loop
                    return trip;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public Trip findTripWithStartStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.STARTSTATION + " = ?";
        Connection conn = null;
        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No start station(s) selected");
        }
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            Trip trip = null;
            ArrayList<Station> stations = tripParameters.getStartStation();
            for (Station station : stations) {
                ps.setLong(1, station.getStationId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                    StationDAO stationDAO = new StationDAO(this.dataSource);
                    Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                    Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                    trip = new Trip(
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                            rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                            rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                            rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                            startStation,
                            endStation,
                            bike);
                    rs.close();
                    ps.close(); // TODO check whether this is working or whether it must out of the for-loop
                    return trip;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public Trip findTripWithEndStations(TripParameters tripParameters) {
        String sql = "SELECT * FROM " + PostgreSQLDatabaseConnection.TRIP +
                " WHERE " + PostgreSQLDatabaseConnection.ENDSTATION + " = ?";
        Connection conn = null;
        if (tripParameters.getStartStation() == null || tripParameters.getStartStation().size() == 0) {
            throw new IllegalArgumentException("No end station(s) selected");
        }
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            Trip trip = null;
            ArrayList<Station> stations = tripParameters.getEndStation();
            for (Station station : stations) {
                ps.setLong(1, station.getStationId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Bike bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
                    StationDAO stationDAO = new StationDAO(this.dataSource);
                    Station startStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION));
                    Station endStation = stationDAO.findStationByID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION));
                    trip = new Trip(
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.STARTTIME).getTime()),
                            new Date (rs.getDate(PostgreSQLDatabaseConnection.ENDTIME).getTime()),
                            rs.getString(PostgreSQLDatabaseConnection.USERTYPE),
                            rs.getShort(PostgreSQLDatabaseConnection.BIRTHYEAR),
                            rs.getShort(PostgreSQLDatabaseConnection.GENDER),
                            startStation,
                            endStation,
                            bike);
                    rs.close();
                    ps.close(); // TODO check whether this is working or whether it must out of the for-loop
                    return trip;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
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

    /*private HashMap<String, Object> addTripParametersToQuery(String sql, TripParameters tripParameters) {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        if (tripParameters.getUserParameters() != null) {
            arguments = addUserParametersToQuery(sql, tripParameters.getUserParameters();
        }
        if (tripParameters.getStartOfTripDistanceRange() > 0) {
            if (alreadyAdded) {
                sql.concat(" AND");
            } else {
                alreadyAdded = true;
            }
            double distanceFromQuery = 3; // TODO: add method call to distance
            sql.concat(" " + Double.toString(distanceFromQuery) + " = ?");

        }

        return arguments;

    }*/
}
