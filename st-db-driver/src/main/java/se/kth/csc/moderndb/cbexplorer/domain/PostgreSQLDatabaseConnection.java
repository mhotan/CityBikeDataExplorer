package se.kth.csc.moderndb.cbexplorer.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

/**
 * This class is for opening a connection to a postgreSQL database specified by the static variables in the class.
 * It also provides methods to create necessary tables for the st-db and methods to insert entries to the created databases.
 * <p/>
 * Created by Jeannine on 14.04.14.
 */
public class PostgreSQLDatabaseConnection {

    private final String DATABASE_NAME = "cityBikeST";
    private final String USERNAME = "vagrant";
    private final String PASSWORD = "vagrant";

    // names of the tables that will be created
    public static final String STATION = "STATION";
    public static final String TRIPTIME = "TRIP_TIME";
    public static final String TRIPROUTE = "TRIP_ROUTE";
    public static final String TRIPSETTS = "TRIP_SETTINGS";

    // names of the attributes in the tables
    private final String ID = "ID";
    private final String NAME = "NAME";
    private final String LONGITUDE = "LONGITUDE";
    private final String LATITUDE = "LATITUDE";
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
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/" + DATABASE_NAME,
                            USERNAME, PASSWORD);

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
     * ID (int, primary key), NAME (text), LONGITUDE (int), LATITUDE (int).
     *
     * @param c connection to the database
     */
    public void createSTATIONTable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + STATION + " " +
                    "(" + ID + " DOUBLE PRECISION PRIMARY KEY NOT NULL," +
                    " " + NAME + " TEXT NOT NULL, " +
                    " " + LONGITUDE + " DOUBLE PRECISION    NOT NULL, " +
                    " " + LATITUDE + " DOUBLE PRECISION NOT NULL)";
            stmt.executeUpdate(sql);
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
     *
     * @param c connection to the database
     */
    public void createTRIPTIMETable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPTIME + " " +
                    "(" + ID + " DOUBLE PRECISION PRIMARY KEY    NOT NULL," +
                    " " + STARTTIME + " DATE    NOT NULL, " +
                    " " + ENDTIME + " DATE  NOT NULL)";
            stmt.executeUpdate(sql);
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
     *
     * @param c connection to the database
     */
    public void createTRIPROUTETable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPROUTE + " " +
                    "(" + ID + " DOUBLE PRECISION PRIMARY KEY     NOT NULL," +
                    " " + STARTSTATION + " DOUBLE PRECISION  NOT NULL, " +
                    " " + ENDSTATION + " DOUBLE PRECISION    NOT NULL)";
            stmt.executeUpdate(sql);
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
     *
     * @param c connection to the database
     */
    public void createTRIPSETTINGSTable(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TRIPSETTS + " " +
                    "(" + ID + " DOUBLE PRECISION PRIMARY KEY    NOT NULL," +
                    " " + BIKEID + " DOUBLE PRECISION    NOT NULL, " +
                    " " + USERTYPE + " TEXT NOT NULL, " +
                    " " + GENDER + " INT    NOT NULL)";
            stmt.executeUpdate(sql);
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
    public void insertIntoSTATION(Connection c, double id, String name, double longitude, double latitude) {
        try {
            Statement stmt = c.createStatement();
            String sql = "INSERT INTO " + STATION + " (" + ID + "," + NAME + "," + LONGITUDE + "," + LATITUDE + ") "
                    + "VALUES (" + id + ", '" + name + "', " + longitude + ", " + latitude + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            //c.close();
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
                    + "VALUES (" + id + ", " + startTime + ", " + endTime + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
           // c.close();
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
            c.commit();
            //c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Inserts an entry into the table TRIP_SETTINGS {@link #createTRIPSETTINGSTable(java.sql.Connection)}
     *
     * @param c        connection to the database
     * @param id       trip id = {bikeid + start time}
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
            c.commit();
           // c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}

