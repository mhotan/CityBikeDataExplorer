package se.kth.csc.moderndb.cbexplorer.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

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

    private final String URL = "jdbc:postgresql://localhost:5432/";

    private final String DATABASE_NAME = "citybike";
    private final String USERNAME = "vagrant";
    private final String PASSWORD = "vagrant";
    // names of the attributes in the tables
    private final String ID = "ID";
    private final String NAME = "NAME";
    private final String POINT = "POINT";
    private final String STARTTIME = "START_TIME";
    private final String ENDTIME = "END_TIME";
    private final String STARTSTATION = "START_STATION_ID";
    private final String ENDSTATION = "END_STATION_ID";
    private final String BIKEID = "BIKE_ID";
    private final String USERTYPE = "USER_TYPE";
    private final String GENDER = "GENDER";


    /**
     * This method opens a connection to a postgreSQL database named like {@value #DATABASE_NAME}.
     * Therefore it uses the username stored in {@value #USERNAME} and the password stored in {@value #PASSWORD}.
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
                    "(" + ID + " DOUBLE PRECISION PRIMARY KEY NOT NULL," +
                    " " + NAME + " TEXT NOT NULL, " +
                    " " + POINT + " GEOMETRY    NOT NULL)";
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
     * Inserts an entry into the table STATION {@link #createSTATIONTable(java.sql.Connection)}.
     *
     * @param c         connection to the database
     * @param id        station id = {latitude, longitude}
     * @param name      station name
     * @param longitude longitude of the station's pos
     * @param latitude  latitude of the station's pos
     */
    public void insertIntoSTATION(Connection c, long id, String name, double longitude, double latitude) {
        try {
            Statement stmt = c.createStatement();
            //((org.postgresql.Connection)c).addDataType("geometry","org.postgis.PGgeometry");
            String sql = "INSERT INTO " + STATION + " (" + ID + "," + NAME + "," + POINT + ") "
                    + "VALUES (?, ?, ST_GeographyFromText('SRID=4326;POINT(' || ? || ' ' || ? || ')')"; //+ id + ", '" + name + "', ST_GeomFromText(\"POINT(" + longitude + ", " + latitude + ")\", 4326))";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            //preparedStatement.setString(3, "POINT(" + longitude + ", " + latitude + ")");
            preparedStatement.setDouble(3, longitude);
            preparedStatement.setDouble(4, latitude);
            preparedStatement.executeUpdate();
            stmt.executeUpdate(sql);
            stmt.close();
            //c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Inserts an entry into the table TRIP_TIME {@link #createTRIPTIMETable(java.sql.Connection)}.
     *
     * @param c         connection to the database
     * @param id        trip id = {bikeid + start time}
     * @param startTime start time of the trip
     * @param endTime   end time of the trip
     */
    public void insertIntoTRIPTIME(Connection c, double id, Date startTime, Date endTime) {
        try {

            Statement stmt = c.createStatement();
            String sql = "INSERT INTO " + TRIPTIME + " (" + ID + "," + STARTTIME + "," + ENDTIME + ") "
                    + "VALUES (" + id + ", '" + startTime + "', '" + endTime + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            //c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    /**
     * Inserts an entry into the table TRIP_ROUTE {@link #createTRIPROUTETable(java.sql.Connection)}.
     *
     * @param c              connection to the database
     * @param id             trip id = {bikeid + start time}
     * @param startStationID id of the trip's start station
     * @param endStationID   id of the trip's end station
     */
    public void insertIntoTRIPROUTE(Connection c, double id, double startStationID, double endStationID) {
        try {

            Statement stmt = c.createStatement();
            String sql = "INSERT INTO " + TRIPROUTE + " (" + ID + "," + STARTSTATION + "," + ENDSTATION + ") "
                    + "VALUES (" + id + ", " + startStationID + ", " + endStationID + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            //c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Inserts an entry into the table TRIP_SETTINGS {@link #createTRIPSETTINGSTable(java.sql.Connection)}
     *
     * @param c        connection to the database
     * @param id       trip id = {bikeid concat start time}
     * @param bikeID   bike id
     * @param usertype type of the user
     * @param gender   gender of the user
     */
    public void insertIntoTRIPSETTINGS(Connection c, double id, long bikeID, String usertype, int gender) {
        try {

            Statement stmt = c.createStatement();
            String sql = "INSERT INTO " + TRIPSETTS + " (" + ID + "," + BIKEID + "," + USERTYPE + "," + GENDER + ") "
                    + "VALUES (" + id + ", " + bikeID + ", '" + usertype + "', " + gender + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            //c.commit();

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

