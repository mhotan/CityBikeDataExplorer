package se.kth.csc.moderndb.cbexplorer;

/**
 * Created by mhotan on 5/2/14.
 */
public class DatabaseConstants {

    // Path to locally stored database
    public static final String DATABASE_PATH = "target/citibike.db";

    // Labels
    public static final String BIKE_LABEL = "Bike";
    public static final String STATION_LABEL = "Station";
    public static final String TRIP_LABEL = "Trip";

    // Relations
    public static final String USES_RELATION = "USES";
    public static final String STARTS_AT_RELATION = "STARTS_AT";
    public static final String ENDS_AT_RELATION = "ENDS_AT";

    // Bike Properties
    public static final String BIKE_ID = "bikeId";

    // Station Properties
    public static final String STATION_ID = "stationId";
    public static final String STATION_NAME = "name";
    public static final String STATION_LOCATION = "location";

    // Trip Properties
    public static final String TRIP_START_TIME = "startTime";
    public static final String TRIP_END_TIME = "endTime";
    public static final String TRIP_USER_TYPE = "userType";
    public static final String TRIP_USER_BIRTH_YEAR = "userBirthYear";
    public static final String TRIP_USER_GENDER = "userGender";

}
