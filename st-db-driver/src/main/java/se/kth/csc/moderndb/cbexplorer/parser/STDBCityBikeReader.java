package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.domain.PSQLConnection;
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

    private PSQLConnection PSQLConnection;
    private Connection c;
    private int tripCount = 0;
    // TODO: database creation via code!?

    /**
     * Constructor.
     * Creates all necessary tables adding the trips.
     */
    public STDBCityBikeReader() {
        // init database connection
        this.PSQLConnection = new PSQLConnection();
        c = this.PSQLConnection.openDB();
        this.PSQLConnection.createAllNecessaryTables(c);
        // after creating the tables close the connection
    }

    @Override
    public void addTrips(Collection<TripData> trips) {
        addTripsToSTATION(trips);
        addTripsToTRIP(trips);

        tripCount += trips.size();
        System.out.println(tripCount + " total trips processed");
    }

    /**
     * Closes the SQL Connection
     * @throws SQLException
     */
    public void close() throws SQLException {
        this.c.close();
    }

    /**
     * Adds the given TripData collection to the STATION Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createSTATIONTable(java.sql.Connection)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToSTATION(Collection<TripData> trips) {
        HashSet<StationData> stations = new HashSet<StationData>();
        for (TripData trip : trips) {
            StationData startStation = new StationData(trip.getStartStationData().getStationId(),
                    trip.getStartStationData().getName(), trip.getStartStationData().getLongitude(),
                    trip.getStartStationData().getLatitude());
            StationData endStation = new StationData(trip.getEndStationData().getStationId(),
                    trip.getEndStationData().getName(), trip.getEndStationData().getLongitude(),
                    trip.getEndStationData().getLatitude());

            stations.add(startStation);
            stations.add(endStation);
        }
        this.PSQLConnection.insertIntoSTATION(this.c, stations);
    }


    /**
     * Adds the given TripData collection to the TRIP Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPTable(java.sql.Connection)}.
     *
     * @param trips parsed trip data
     */
    private void addTripsToTRIP(Collection<TripData> trips) {
        this.PSQLConnection.insertIntoTRIP(this.c, trips);
    }
}

