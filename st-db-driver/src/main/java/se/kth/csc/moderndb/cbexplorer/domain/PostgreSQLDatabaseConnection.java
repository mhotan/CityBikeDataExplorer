package se.kth.csc.moderndb.cbexplorer.domain;

import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class is for opening a connection to a postgreSQL database specified by the static variables in the class.
 * It also provides methods to create necessary tables for the st-db and methods to insert entries to the created databases.
 * <p/>
 * Created by Jeannine on 14.04.14.
 */
public class PostgreSQLDatabaseConnection {

    // names of the tables that will be created
    public static final String STATION = "station";
    public static final String TRIP = "trip";
    public static final String URL = "jdbc:postgresql://localhost:5432/";
    public static final String DRIVER_FULL_NAME = "org.postgresql.Driver";

    public static final String DATABASE_NAME = "citybike";
    public static final String USERNAME = "vagrant";
    public static final String PASSWORD = "vagrant";
    // names of the attributes in the tables
    public static final String STATIONID = "station_id";
    public static final String NAME = "name";
    public static final String POINT = "point";
    public static final String STARTTIME = "start_time";
    public static final String ENDTIME = "end_time";
    public static final String STARTSTATION = "start_station_id";
    public static final String ENDSTATION = "end_station_id";
    public static final String BIKEID = "bike_id";
    public static final String USERTYPE = "user_type";
    public static final String GENDER = "gender";
    public static final String BIRTHYEAR = "birth_year";
    public static final String DURATION = "duration";


    /**
     * This method opens a connection to a postgreSQL database named like {@link #DATABASE_NAME}.
     * Therefore it uses the username stored in {@link #USERNAME} and the password stored in {@link #PASSWORD}.
     *
     * @return the connection to the opened database or null if opening was not successful
     */
    public Connection openDB() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER_FULL_NAME).newInstance();
            c = DriverManager
                    .getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;

    }

    /**
     * Creates the database table STATION having the following structure:
     * STATIONID (int, primary key), NAME (text), POINT (int), LATITUDE (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createSTATIONTable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + STATION + " " +
                    "(" + STATIONID + " BIGINT PRIMARY KEY NOT NULL," +
                    " " + NAME + " TEXT NOT NULL, " +
                    " " + POINT + " geography(POINT,4326)    NOT NULL);";
            stmt.executeUpdate(sql);
            String index = "CREATE INDEX " + STATION + "_INDEX_" + STATIONID + " ON " + STATION + "(" + STATIONID + ")";
            stmt.executeUpdate(index);
            String rule = "CREATE OR REPLACE RULE \"station_on_duplicate_ignore\" AS ON INSERT TO \"" + STATION + "\" WHERE EXISTS(SELECT 1 FROM " + STATION + " WHERE (" + STATIONID + ")=(NEW." + STATIONID + ")) DO INSTEAD NOTHING;";
            stmt.execute(rule);
            stmt.close();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /**
     * Creates the database table TRIP_ROUTE having the following structure:
     * STATIONID (int, primary key), START_STATION_ID (int), END_STATION_ID (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createTRIPROUTETable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIP + " " +
                    "(" + BIKEID + " BIGINT      NOT NULL," +
                    " " + STARTTIME + " TIMESTAMP  NOT NULL, " +
                    " " + ENDTIME + " TIMESTAMP  NOT NULL," +
                    " " + DURATION + " BIGINT NOT NULL," +
                    " " + STARTSTATION + " BIGINT  NOT NULL, " +
                    " " + ENDSTATION + " BIGINT    NOT NULL," +
                    " " + USERTYPE + " TEXT NOT NULL, " +
                    " " + BIRTHYEAR + " INT NOT NULL, " +
                    " " + GENDER + " INT    NOT NULL," +
                    " PRIMARY KEY(" + BIKEID + "," + STARTTIME + "));";
            stmt.executeUpdate(sql);
            String index = "CREATE INDEX " + TRIP + "_INDEX_" + BIKEID + " ON " + TRIP + "(" + BIKEID + ");";
            stmt.executeUpdate(index);
            index = "CREATE INDEX " + TRIP + "_INDEX_TRIPID" + " ON " + TRIP + "(" + BIKEID + "," + STARTTIME + ");";
            stmt.executeUpdate(index);
            String rule = "CREATE OR REPLACE RULE \"trip_route_on_duplicate_ignore\" AS ON INSERT TO \"" +
                    TRIP + "\" WHERE EXISTS(SELECT 1 FROM " + TRIP + " WHERE (" + BIKEID + "," +
                    STARTTIME + ")=(NEW." + BIKEID + ", NEW." + STARTTIME + ")) DO INSTEAD NOTHING;";
            stmt.execute(rule);

            stmt.close();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /**
     * Creates the tables STATION {@link #createSTATIONTable(java.sql.Connection)},
     * TRIP_ROUTE {@link #createTRIPROUTETable(java.sql.Connection)}.
     *
     * @param c connection to the database
     */
    public void createAllNecessaryTables(Connection c) {
        createSTATIONTable(c);
        createTRIPROUTETable(c);
    }

    /**
     * Inserts all data sets from {@param stations} into the table STATION {@link #createSTATIONTable(java.sql.Connection)}.
     *
     * @param c        connection to the database
     * @param stations set containing the data of stations that should be put into the table
     */
    public void insertIntoSTATION(Connection c, HashSet<StationData> stations) {
        try {
            String sql = "INSERT INTO " + STATION + " (" + STATIONID + "," + NAME + "," + POINT + ") "
                    + "VALUES (?, ?, ST_GeographyFromText('SRID=4326;POINT(' || ? || ' ' || ? || ')'));";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (StationData station : stations) {
                preparedStatement.setLong(1, station.getStationId());
                preparedStatement.setString(2, station.getName());
                preparedStatement.setDouble(3, station.getLongitude());
                preparedStatement.setDouble(4, station.getLatitude());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /**
     * Inserts table specific data from {@param #tripDate} into the table TRIP_ROUTE {@link #createTRIPROUTETable(java.sql.Connection)}.
     *
     * @param c        connection to the database
     * @param tripDate set containing data of the given trips
     */
    public void insertIntoTRIP(Connection c, Collection<TripData> tripDate) {
        try {
            String sql = "INSERT INTO " + TRIP + " (" + BIKEID + "," + STARTTIME + "," + ENDTIME + "," + DURATION + "," +
                    STARTSTATION + "," + ENDSTATION + "," + USERTYPE + "," + BIRTHYEAR + "," + GENDER + ") "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (TripData tripDataObject : tripDate) {
                preparedStatement.setLong(1, tripDataObject.getBikeData().getId());
                preparedStatement.setTimestamp(2, new Timestamp(tripDataObject.getStartTime().getTime()));
                preparedStatement.setTimestamp(3, new java.sql.Timestamp(tripDataObject.getEndTime().getTime()));
                preparedStatement.setLong(4, tripDataObject.getDuration());
                preparedStatement.setLong(5, tripDataObject.getStartStationData().getStationId());
                preparedStatement.setLong(6, tripDataObject.getEndStationData().getStationId());
                preparedStatement.setString(7, tripDataObject.getUserType());
                preparedStatement.setInt(8, tripDataObject.getUserBirthYear());
                preparedStatement.setInt(9, tripDataObject.getUserGender());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}

