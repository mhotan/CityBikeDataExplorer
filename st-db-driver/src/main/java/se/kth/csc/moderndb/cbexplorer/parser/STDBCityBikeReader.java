package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.data.TripDataObject;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Handles the data parsed from the data files and writes it into the appropriate tables.
 * If there are no appropriate tables yet, it creates them.
 * <p/>
 * Created by Jeannine on 14.04.14.
 */
public class STDBCityBikeReader implements CitiBikeReader {

    // TODO Documentation
    // longitude and latitude of the stations will be multiplied with this constant to get the correspondent decimal places for building the STATION_ID in the STATION table {@link #PostgreSQLDatabaseConnection.createSTATIONTable()}.
    private final int DECIMAL_PLACE_FOR_ID = 100000;

    private PostgreSQLDatabaseConnection postgreSQLDatabaseConnection;
    private Connection c;
    private int tripCount = 0;
    // TODO: database creation via code!?

    /**
     * Constructor.
     * Creates all necessary tables adding the trips.
     */
    public STDBCityBikeReader() {
        // init database connection
        this.postgreSQLDatabaseConnection = new PostgreSQLDatabaseConnection();
        c = this.postgreSQLDatabaseConnection.openDB();
        this.postgreSQLDatabaseConnection.createAllNecessaryTables(c);
        // after creating the tables close the connection
    }

    @Override
    public void addTrips(Collection<TripData> trips) {
        addTripsToSTATION(trips);
        HashSet<TripDataObject> tripObjects = createTripDataObjectsFromTrips(trips);
        addTripsToTRIPROUTE(tripObjects);
        addTripsToTRIPTIME(tripObjects);
        addTripsToTRIPSETTINGS(tripObjects);

        tripCount += trips.size();
        System.out.println(tripCount + " total trips processed");
    }

    public void close() throws SQLException {
        this.c.close();
    }

    /**
     * Adds the given TripData collection to the STATION Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createSTATIONTable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateStationIDString(se.kth.csc.moderndb.cbexplorer.parser.data.StationData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToSTATION(Collection<TripData> trips) {
        HashSet<StationData> stations = new HashSet<StationData>();
        for (TripData trip : trips) {
            // id for Station consists of id = {latitude, longitude}
            long id_startStation = calculateStationIDString(trip.getStartStationData());
            long id_endStation = calculateStationIDString(trip.getEndStationData());

            StationData startStation = new StationData(id_startStation, trip.getStartStationData().getName(), trip.getStartStationData().getLongitude(), trip.getStartStationData().getLatitude());
            StationData endStation = new StationData(id_endStation, trip.getEndStationData().getName(), trip.getEndStationData().getLongitude(), trip.getEndStationData().getLatitude());

            stations.add(startStation);
            stations.add(endStation);
        }
        this.postgreSQLDatabaseConnection.insertIntoSTATION(this.c, stations);
    }

    /**
     * Adds the given TripData collection to the TRIPTIME Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPTIMETable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPTIME(HashSet<TripDataObject> trips) {
        this.postgreSQLDatabaseConnection.insertIntoTRIPTIME(this.c, trips);
    }

    /**
     * Adds the given TripData collection to the TRIPROUTE Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPROUTETable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPROUTE(HashSet<TripDataObject> trips) {
        this.postgreSQLDatabaseConnection.insertIntoTRIPROUTE(this.c, trips);
    }

    /**
     * Adds the given TripData collection to the TRIPSETTINGS Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPSETTINGSTable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPSETTINGS(HashSet<TripDataObject> trips) {
        this.postgreSQLDatabaseConnection.insertIntoTRIPSETTINGS(this.c, trips);
    }

    /**
     * Calculates the stationID for the database tables. Therefore, it concats the latitude of the station to the longitude of the station.
     * E.g.: station.latitude = 123, station. longitude = 456 -> station.id = 123456
     *
     * @param station station for which the ID should be calculated
     * @return stationID
     */
    private long calculateStationIDString(StationData station) {
        int lat = (int) Math.abs(station.getLatitude() * DECIMAL_PLACE_FOR_ID);
        int lon = (int) Math.abs(station.getLongitude() * DECIMAL_PLACE_FOR_ID);
        String res = String.valueOf(lat).concat(String.valueOf(lon));
        return Long.parseLong(String.valueOf(lat).concat(String.valueOf(lon)));
    }

    /**
     * Calculates the tripID for the database tables. Therefore, it concats the bikeID with the starttime of the trip.
     *
     * @param trip trip for which the ID should be calculated
     * @return tripID
     */
    private long calculateTripID(TripData trip) {
        String bikeID = Long.toString(trip.getBikeData().getId());
        long start = (long) trip.getStartTime().getTime();
        return Long.parseLong(bikeID.concat(Long.toString(start)));
    }

    /**
     * Creates an HashSet of TripDataObjects {@see #se.kth.csc.moderndb.cbexplorer.data.TripDataObject}.
     * This is needed to fill the database tables with appropriate data.
     *
     * @param trips TripData arise from parsing cvs-files
     * @return ready to fill into tables TripDataObjects
     */
    private HashSet<TripDataObject> createTripDataObjectsFromTrips(Collection<TripData> trips) {
        HashSet<TripDataObject> tripDataObjects = new HashSet<TripDataObject>();
        for (TripData trip : trips) {
            long tripID = calculateTripID(trip);
            long startStationID = calculateStationIDString(trip.getStartStationData());
            long endStationID = calculateStationIDString(trip.getEndStationData());
            TripDataObject tripDataObject = new TripDataObject(tripID, startStationID, endStationID, trip.getStartTime(), trip.getEndTime(), trip.getBikeData().getId(), trip.getUserGender(), trip.getUserBirthYear(), trip.getUserType());
            tripDataObjects.add(tripDataObject);
        }
        return tripDataObjects;
    }
}

