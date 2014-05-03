package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Handles the data parsed from the data files and writes it into the appropriate tables.
 * If there are no appropriate tables yet, it creates them.
 * <p/>
 * Created by Jeannine on 14.04.14.
 */
public class STDBCityBikeReader implements CitiBikeReader {

    // longitude and latitude of the stations will be multiplied with this constant to get the correspondent decimal places for building the STATION_ID in the STATION table {@link #PostgreSQLDatabaseConnection.createSTATIONTable()}.
    private final int DECIMAL_PLACE_FOR_ID = 100000;

    private PostgreSQLDatabaseConnection postgreSQLDatabaseConnection;
    private Connection c;
    private int tripCount = 0;
    // TODO: database creation via code!?


    public STDBCityBikeReader() {
        // init database connection
        this.postgreSQLDatabaseConnection = new PostgreSQLDatabaseConnection();
        c = this.postgreSQLDatabaseConnection.openDB();
        this.postgreSQLDatabaseConnection.createAllNecessaryTables(c);
        // after creating the tables close the connection
        try {
            this.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTrips(Collection<TripData> trips) {
        addTripsToSTATION(trips);
        addTripsToTRIPROUTE(trips);
        addTripsToTRIPTIME(trips);
        addTripsToTRIPSETTINGS(trips);
        tripCount += trips.size();
        System.out.println(tripCount + " total trips processed");
    }

    /**
     * Adds the given TripData collection to the STATION Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createSTATIONTable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateStationIDString(se.kth.csc.moderndb.cbexplorer.parser.data.StationData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToSTATION(Collection<TripData> trips) {
        this.c = this.postgreSQLDatabaseConnection.openDB();
        HashMap<String, String> stationName = new HashMap<String, String>();
        // find fitting latitude and longitude
        HashMap<String, Double> stationLat = new HashMap<String, Double>();
        HashMap<String, Double> stationLong = new HashMap<String, Double>();
        for (TripData trip : trips) {
            // id for Station consists of id = {latitude, longitude}
            String id_startStation = calculateStationIDString(trip.getStartStationData());
            String id_endStation = calculateStationIDString(trip.getEndStationData());

            stationName.put(id_startStation, trip.getStartStationData().getName());
            stationLat.put(id_startStation, trip.getStartStationData().getLatitude());
            stationLong.put(id_startStation, trip.getStartStationData().getLongitude());

            stationName.put(id_endStation, trip.getEndStationData().getName());
            stationLat.put(id_endStation, trip.getEndStationData().getLatitude());
            stationLong.put(id_endStation, trip.getEndStationData().getLongitude());

        }

        // add to database
        for (String stationID : stationName.keySet()) {
            this.postgreSQLDatabaseConnection.insertIntoSTATION(this.c, Long.parseLong(stationID), stationName.get(stationID), stationLong.get(stationID), stationLat.get(stationID));
        }
        try {
            this.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given TripData collection to the TRIPTIME Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPTIMETable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPTIME(Collection<TripData> trips) {
        this.c = this.postgreSQLDatabaseConnection.openDB();
        for (TripData trip : trips) {
            double tripID = calculateTripID(trip);
            this.postgreSQLDatabaseConnection.insertIntoTRIPTIME(this.c, tripID, trip.getStartTime(), trip.getEndTime());
        }
        try {
            this.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given TripData collection to the TRIPROUTE Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPROUTETable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPROUTE(Collection<TripData> trips) {
        this.c = this.postgreSQLDatabaseConnection.openDB();
        for (TripData trip : trips) {
            double tripID = calculateTripID(trip);
            double startStation = Long.parseLong(calculateStationIDString(trip.getStartStationData()));
            double endStation = Long.parseLong(calculateStationIDString(trip.getEndStationData()));
            this.postgreSQLDatabaseConnection.insertIntoTRIPROUTE(this.c, tripID, startStation, endStation);
        }
        try {
            this.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given TripData collection to the TRIPSETTINGS Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPSETTINGSTable(java.sql.Connection)}.
     * It calculates the StationID with {@link #calculateTripID(se.kth.csc.moderndb.cbexplorer.parser.data.TripData)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIPSETTINGS(Collection<TripData> trips) {
        this.c = this.postgreSQLDatabaseConnection.openDB();
        for (TripData trip : trips) {
            double tripID = calculateTripID(trip);
            this.postgreSQLDatabaseConnection.insertIntoTRIPSETTINGS(this.c, tripID, trip.getBikeData().getId(), trip.getUserType(), trip.getUserGender());
        }
        try {
            this.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the stationID for the database tables. Therefore, it concats the latitude of the station to the longitude of the station.
     * E.g.: station.latitude = 123, station. longitude = 456 -> station.id = 123456
     *
     * @param station station for which the ID should be calculated
     * @return stationID
     */
    private String calculateStationIDString(StationData station) {
        int lat = (int) Math.abs(station.getLatitude()* DECIMAL_PLACE_FOR_ID);
        int lon = (int) Math.abs(station.getLongitude()* DECIMAL_PLACE_FOR_ID);
        String res = String.valueOf(lat).concat(String.valueOf(lon));
        return String.valueOf(lat).concat(String.valueOf(lon));
    }

    /**
     * Calculates the tripID for the database tables. Therefore, it concats the bikeID with the starttime of the trip.
     *
     * @param trip trip for which the ID should be calculated
     * @return tripID
     */
    private double calculateTripID(TripData trip) {
        String bikeID = Long.toString(trip.getBikeData().getId());
        double start = (double) trip.getStartTime().getTime();
        return Double.parseDouble(bikeID.concat(Double.toString(start)));
    }
}

