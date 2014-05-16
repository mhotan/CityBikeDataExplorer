package se.kth.csc.moderndb.cbexplorer;

/**
 * This class is for opening a connection to a postgreSQL database specified by the static variables in the class.
 * It also provides methods to create necessary tables for the st-db and methods to insert entries to the created databases.
 * <p/>
 * Created by Jeannine on 14.04.14.
 */
public class PSQLConnection {

    // names of the tables that will be created
    public static final String STATION = "station";
    public static final String TRIP = "trip";

    // names of the attributes in the tables
    public static final String STATION_ID = "station_id";
    public static final String NAME = "name";
    public static final String POINT = "point";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String START_STATION = "start_station_id";
    public static final String END_STATION = "end_station_id";
    public static final String BIKE_ID = "bike_id";
    public static final String USER_TYPE = "user_type";
    public static final String GENDER = "gender";
    public static final String BIRTH_YEAR = "birth_year";
    public static final String DURATION = "duration";

    // SQL String statements
    private static final String SRID = "4326";

    // Create the tables.
    public static final String CREATE_TRIP_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + TRIP + " " +
            "(" + BIKE_ID + " BIGINT      NOT NULL," +
            " " + START_TIME + " TIMESTAMP  NOT NULL, " +
            " " + END_TIME + " TIMESTAMP  NOT NULL," +
            " " + DURATION + " BIGINT NOT NULL," +
            " " + START_STATION + " BIGINT  NOT NULL, " +
            " " + END_STATION + " BIGINT    NOT NULL, " +
            " " + USER_TYPE + " TEXT NOT NULL, " +
            " " + BIRTH_YEAR + " INT, " +
            " " + GENDER + " INT    NOT NULL," +
            " PRIMARY KEY(" + BIKE_ID + "," + START_TIME + "))";;
    public static final String CREATE_STATION_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + STATION + " " +
                    "(" + STATION_ID + " BIGINT PRIMARY KEY NOT NULL," +
                    " " + NAME + " TEXT NOT NULL, " +
                    " " + POINT + " geography(POINT," + SRID + ") NOT NULL)";

    // Trip Table Indexes
    private static final String DROP_INDEX_IF_EXISTS = "DROP INDEX IF EXISTS ";
    private static final String CREATE_INDEX = "CREATE INDEX ";
    private static final String STATION_ID_INDEX = STATION + "_INDEX_" + STATION_ID;
    private static final String STATION_POINT_INDEX = STATION + "_INDEX_" + POINT;
    private static final String TRIP_ID_INDEX = TRIP + "_INDEX_TRIPID";
    private static final String TRIP_BIKE_INDEX = TRIP + "_INDEX_" + BIKE_ID;

    // Station Indexes.
    public static final String CREATE_STATION_ID_INDEX = CREATE_INDEX + STATION_ID_INDEX +
            " ON " + STATION + "(" + STATION_ID + ")";
    public static final String DROP_STATION_ID_INDEX = DROP_INDEX_IF_EXISTS + STATION_ID_INDEX;
    public static final String CREATE_STATION_POINT_INDEX = CREATE_INDEX + STATION_POINT_INDEX +
            " ON " + STATION + "(" + POINT + ")";
    public static final String DROP_STATION_POINT_INDEX = DROP_INDEX_IF_EXISTS + STATION_POINT_INDEX;

    // Trip Indexes
    public static final String CREATE_TRIP_BIKE_INDEX = CREATE_INDEX + TRIP_BIKE_INDEX +
            " ON " + TRIP + "(" + BIKE_ID + ")";
    public static final String DROP_TRIP_BIKE_INDEX = DROP_INDEX_IF_EXISTS + TRIP_BIKE_INDEX;
    public static final String CREATE_TRIP_ID_INDEX = CREATE_INDEX + TRIP_ID_INDEX + " ON " + TRIP +
            "(" + BIKE_ID + "," + START_TIME + ")";
    public static final String DROP_TRIP_ID_INDEX = DROP_INDEX_IF_EXISTS + TRIP_ID_INDEX;

    // Rules for inserts
    public static final String INSERT_ON_STATION_RULE =
            "CREATE OR REPLACE RULE \"station_on_duplicate_ignore\" AS ON INSERT TO \"" + STATION +
            "\" WHERE EXISTS(SELECT 1 FROM " + STATION + " WHERE (" + STATION_ID +
            ")=(NEW." + STATION_ID + ")) DO INSTEAD NOTHING";
    public static final String INSERT_ON_TRIP_RULE =
            "CREATE OR REPLACE RULE \"trip_route_on_duplicate_ignore\" AS ON INSERT TO \"" +
            TRIP + "\" WHERE EXISTS(SELECT 1 FROM " + TRIP + " WHERE (" + BIKE_ID + "," +
            START_TIME + ")=(NEW." + BIKE_ID + ", NEW." + START_TIME + ")) DO INSTEAD NOTHING;";


    // Insert into the tables statements.
    public static final String INSERT_STATION_STATEMENT =
            "INSERT INTO " + STATION + " (" + STATION_ID + "," + NAME + "," + POINT + ") "
                    + "VALUES (?, ?, ST_GeographyFromText('SRID=4326;POINT(' || ? || ' ' || ? || ')'))";
    public static final String INSERT_TRIP_STATEMENT =
            "INSERT INTO " + TRIP + " (" + BIKE_ID + "," + START_TIME + "," + END_TIME + "," + DURATION + "," +
            START_STATION + "," + END_STATION + "," + USER_TYPE + "," + BIRTH_YEAR + "," + GENDER + ") "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

//    /**
//     * Creates the database table STATION having the following structure:
//     * STATION_ID (int, primary key), NAME (text), POINT (int), LATITUDE (int).
//     * It also adds a rule to ignore duplicated entries in the database.
//     *
//     * @param c connection to the database
//     */
//    public void createSTATIONTable(Connection c) throws SQLException {
//        Statement stmt = c.createStatement();
//        String sql = CREATE_STATION_TABLE_QUERY;
//        stmt.executeUpdate(sql);
//        String dropIndex = DROP_STATION_ID_INDEX;
//        stmt.executeUpdate(dropIndex);
//        String index = CREATE_STATION_ID_INDEX;
//        stmt.executeUpdate(index);
//        dropIndex = DROP_STATION_POINT_INDEX;
//        stmt.executeUpdate(dropIndex);
//        index = CREATE_STATION_POINT_INDEX;
//        stmt.executeUpdate(index);
//        String rule = INSERT_ON_STATION_RULE;
//        stmt.execute(rule);
//        stmt.close();
//    }
//
//
//    /**
//     * Creates the database table TRIP_ROUTE having the following structure:
//     * STATION_ID (int, primary key), START_STATION_ID (int), END_STATION_ID (int).
//     * It also adds a rule to ignore duplicated entries in the database.
//     *
//     * @param c connection to the database
//     */
//    public void createTRIPROUTETable(Connection c) throws SQLException {
//        Statement stmt = c.createStatement();
//        String sql = CREATE_TRIP_TABLE_QUERY;
//        stmt.executeUpdate(sql);
//        String deleteIndex = DROP_TRIP_BIKE_INDEX;
//        stmt.executeUpdate(deleteIndex);
//        String index = CREATE_TRIP_BIKE_INDEX;
//        stmt.executeUpdate(index);
//        deleteIndex = DROP_TRIP_ID_INDEX;
//        stmt.executeUpdate(deleteIndex);
//        index = CREATE_TRIP_ID_INDEX;
//        stmt.executeUpdate(index);
//        String rule = INSERT_ON_TRIP_RULE;
//        stmt.execute(rule);
//
//        stmt.close();
//    }

}

