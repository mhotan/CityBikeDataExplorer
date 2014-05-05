package se.kth.csc.moderndb.cbexplorer.domain;

import se.kth.csc.moderndb.cbexplorer.data.TripDataObject;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public static final String TRIPTIME = "trip_time";
    public static final String TRIPROUTE = "trip_route";
    public static final String TRIPSETTS = "trip_settings";

    public static final String URL = "jdbc:postgresql://localhost:5432/";

    public static final String DATABASE_NAME = "citybike";
    public static final String USERNAME = "vagrant";
    public static final String PASSWORD = "vagrant";
    // names of the attributes in the tables
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String POINT = "point";
    public static final String STARTTIME = "start_time";
    public static final String ENDTIME = "end_time";
    public static final String STARTSTATION = "start_station_id";
    public static final String ENDSTATION = "end_station_id";
    public static final String BIKEID = "bike_id";
    public static final String USERTYPE = "user_type";
    public static final String GENDER = "gender";


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
            Class.forName("org.postgresql.Driver").newInstance();
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
     * ID (int, primary key), NAME (text), POINT (int), LATITUDE (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createSTATIONTable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + STATION + " " +
                    "(" + ID + " BIGINT PRIMARY KEY NOT NULL," +
                    " " + NAME + " TEXT NOT NULL, " +
                    " " + POINT + " geography(POINT,4326)    NOT NULL)";
            stmt.executeUpdate(sql);
            String rule = "CREATE OR REPLACE RULE \"station_on_duplicate_ignore\" AS ON INSERT TO \"" + STATION + "\" WHERE EXISTS(SELECT 1 FROM " + STATION + " WHERE (" + ID + ")=(NEW." + ID + ")) DO INSTEAD NOTHING;";
            stmt.execute(rule);
            stmt.close();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates the database table TRIP_TIME having the following structure:
     * ID (int, primary key), START_TIME (int), END_TIME (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createTRIPTIMETable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPTIME + " " +
                    "(" + ID + " BIGINT PRIMARY KEY    NOT NULL," +
                    " " + STARTTIME + " DATE    NOT NULL, " +
                    " " + ENDTIME + " DATE  NOT NULL)";
            stmt.executeUpdate(sql);
            String rule = "CREATE OR REPLACE RULE \"trip_time_on_duplicate_ignore\" AS ON INSERT TO \"" + TRIPTIME + "\" WHERE EXISTS(SELECT 1 FROM " + TRIPTIME + " WHERE (" + ID + ")=(NEW." + ID + ")) DO INSTEAD NOTHING;";
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
     * ID (int, primary key), START_STATION_ID (int), END_STATION_ID (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createTRIPROUTETable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPROUTE + " " +
                    "(" + ID + " BIGINT PRIMARY KEY     NOT NULL," +
                    " " + STARTSTATION + " BIGINT  NOT NULL, " +
                    " " + ENDSTATION + " BIGINT    NOT NULL)";
            stmt.executeUpdate(sql);
            String rule = "CREATE OR REPLACE RULE \"trip_route_on_duplicate_ignore\" AS ON INSERT TO \"" + TRIPROUTE + "\" WHERE EXISTS(SELECT 1 FROM " + TRIPROUTE + " WHERE (" + ID + ")=(NEW." + ID + ")) DO INSTEAD NOTHING;";
            stmt.execute(rule);

            stmt.close();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates the database table TRIP_SETTINGS having the following structure:
     * ID (int, primary key), BIKE_ID (int), USER_TYPE (text), GENDER (int).
     * It also adds a rule to ignore duplicated entries in the database.
     *
     * @param c connection to the database
     */
    public void createTRIPSETTINGSTable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPSETTS + " " +
                    "(" + ID + " BIGINT PRIMARY KEY    NOT NULL," +
                    " " + BIKEID + " BIGINT    NOT NULL, " +
                    " " + USERTYPE + " TEXT NOT NULL, " +
                    " " + GENDER + " INT    NOT NULL)";
            stmt.executeUpdate(sql);
            String rule = "CREATE OR REPLACE RULE \"trip_setts_on_duplicate_ignore\" AS ON INSERT TO \"" + TRIPSETTS + "\" WHERE EXISTS(SELECT 1 FROM " + TRIPSETTS + " WHERE (" + ID + ")=(NEW." + ID + ")) DO INSTEAD NOTHING;";
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
     * TRIP_ROUTE {@link #createTRIPROUTETable(java.sql.Connection)},
     * TRIP_TIME {@link #createTRIPTIMETable(java.sql.Connection)} and
     * TRIP_SETTINGS {@link #createTRIPSETTINGSTable(java.sql.Connection)}.
     *
     * @param c connection to the database
     */
    public void createAllNecessaryTables(Connection c) {
        createSTATIONTable(c);
        createTRIPROUTETable(c);
        createTRIPTIMETable(c);
        createTRIPSETTINGSTable(c);
    }

    /**
     * Inserts all data sets from {@param stations} into the table STATION {@link #createSTATIONTable(java.sql.Connection)}.
     *
     * @param c        connection to the database
     * @param stations set containing the data of stations that should be put into the table
     */
    public void insertIntoSTATION(Connection c, HashSet<StationData> stations) {
        try {
            String sql = "INSERT INTO " + STATION + " (" + ID + "," + NAME + "," + POINT + ") "
                    + "VALUES (?, ?, ST_GeographyFromText('SRID=4326;POINT(' || ? || ' ' || ? || ')'))";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (StationData station : stations) {
                preparedStatement.setLong(1, station.getStationId());
                preparedStatement.setString(2, station.getName());
                preparedStatement.setDouble(3, station.getLongitude());
                preparedStatement.setDouble(4, station.getLatitude());
                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Inserts table specific data from {@param #tripDataObjects} into the table TRIP_TIME {@link #createTRIPTIMETable(java.sql.Connection)}.
     *
     * @param c               connection to the database
     * @param tripDataObjects set containing data of the given trips
     */
    public void insertIntoTRIPTIME(Connection c, HashSet<TripDataObject> tripDataObjects) {
        try {
            String sql = "INSERT INTO " + TRIPTIME + " (" + ID + "," + STARTTIME + "," + ENDTIME + ") "
                    + "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (TripDataObject tripDataObject : tripDataObjects) {
                preparedStatement.setLong(1, tripDataObject.getTripID());
                preparedStatement.setDate(2, new java.sql.Date(tripDataObject.getStartTime().getTime()));
                preparedStatement.setDate(3, new java.sql.Date(tripDataObject.getEndTime().getTime()));
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    /**
     * Inserts table specific data from {@param #tripDataObjects} into the table TRIP_ROUTE {@link #createTRIPROUTETable(java.sql.Connection)}.
     *
     * @param c               connection to the database
     * @param tripDataObjects set containing data of the given trips
     */
    public void insertIntoTRIPROUTE(Connection c, HashSet<TripDataObject> tripDataObjects) {
        try {
            String sql = "INSERT INTO " + TRIPROUTE + " (" + ID + "," + STARTSTATION + "," + ENDSTATION + ") "
                    + "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (TripDataObject tripDataObject : tripDataObjects) {
                preparedStatement.setLong(1, tripDataObject.getTripID());
                preparedStatement.setLong(2, tripDataObject.getStartStationID());
                preparedStatement.setLong(3, tripDataObject.getEndStationID());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Inserts table specific data from {@param #tripDataObjects} into the table TRIP_SETTINGS {@link #createTRIPSETTINGSTable(java.sql.Connection)}
     *
     * @param c               connection to the database
     * @param tripDataObjects set containing data of the given trips
     */
    public void insertIntoTRIPSETTINGS(Connection c, HashSet<TripDataObject> tripDataObjects) {
        try {
            String sql = "INSERT INTO " + TRIPSETTS + " (" + ID + "," + BIKEID + "," + USERTYPE + "," + GENDER + ") "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            for (TripDataObject tripDataObject : tripDataObjects) {
                preparedStatement.setLong(1, tripDataObject.getTripID());
                preparedStatement.setLong(2, tripDataObject.getBikeID());
                preparedStatement.setString(3, tripDataObject.getUserDescription());
                preparedStatement.setInt(4, tripDataObject.getGender());
                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /*private void creatingInitDatabase() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(URLPROGRES, USERNAME, PASSWORD);
            stmt = c.createStatement();
            String sql = "CREATE DATABASE " + DATABASE_NAME;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }*/
}

