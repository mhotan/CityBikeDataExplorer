package se.kth.csc.moderndb.cbexplorer;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Handles the data parsed from the data files and writes it into the appropriate tables.
 * If there are no appropriate tables yet, it creates them.
 * <p/>
 * Created by Jeannine on 14.04.14. Michael Hotan
 */
public class STDBCityBikeReader implements CitiBikeReader {

    /**
     * Template to interact with the data source.
     */
    private final JdbcTemplate template;

    /**
     * Constructor.
     * Creates all necessary tables adding the trips.
     */
    public STDBCityBikeReader(DataSource dataSource) {
        if (dataSource == null)
            throw new NullPointerException(getClass().getSimpleName() + " Can't have null data source.");
        this.template = new JdbcTemplate(dataSource);

        // Setup the tables in the database
        setupTables();
    }

    /**
     * Sets up all the internal tables of the database.
     */
    private void setupTables() {
        setupStationsTable();
        setupTripTable();
    }

    /**
     * Set up a table for Stations
     */
    private void setupStationsTable() {
        // Create the station table
        template.update(PSQLConnection.CREATE_STATION_TABLE_QUERY);

        // Drop all the old indexes
        template.update(PSQLConnection.DROP_STATION_ID_INDEX);
        template.update(PSQLConnection.DROP_STATION_POINT_INDEX);

        // Recreate the indexes
        template.update(PSQLConnection.CREATE_STATION_ID_INDEX);
        template.update(PSQLConnection.CREATE_STATION_POINT_INDEX);

        // Create the insertion Rule for stations.
        template.execute(PSQLConnection.INSERT_ON_STATION_RULE);
    }

    private void setupTripTable() {
        // Create the station table
        template.update(PSQLConnection.CREATE_TRIP_TABLE_QUERY);

        // Drop all the old indexes
        template.update(PSQLConnection.DROP_TRIP_BIKE_INDEX);
        template.update(PSQLConnection.DROP_TRIP_ID_INDEX);

        // Recreate the indexes
        template.update(PSQLConnection.CREATE_TRIP_BIKE_INDEX);
        template.update(PSQLConnection.CREATE_TRIP_ID_INDEX);

        // Create the insertion Rule for stations.
        template.execute(PSQLConnection.INSERT_ON_TRIP_RULE);
    }

    @Override
    public void addTrips(Collection<Trip> trips) {
        // Process all the stations
        addStationsFromTrips(new ArrayList<Trip>(trips));

        // Process all the trips
        addTripsPrivate(trips);
    }

    /**
     * Adds the given TripData collection to the STATION Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createSTATIONTable(java.sql.Connection)}.
     *
     * @param trips parsed trip data
     */
    private void addStationsFromTrips(List<Trip> trips) {
        // Creating a set and override the equals method ensures
        // that stations are not add multiple times.
        final Set<Station> stationSet = new HashSet<Station>();

        // Obtain all the stations from all the trips first.
        for (Trip trip : trips) {
            Station startStation = trip.getStartsAt();
            Station endStation = trip.getEndsAt();
            stationSet.add(startStation);
            stationSet.add(endStation);
        }

        // Do the batch insert to add all the stations.
        final List<Station> stations = new ArrayList<Station>(stationSet);
        template.batchUpdate(PSQLConnection.INSERT_STATION_STATEMENT,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Station station = stations.get(i);
                        ps.setLong(1, station.getStationId());
                        ps.setString(2, station.getName());
                        ps.setDouble(3, station.getLongitude());
                        ps.setDouble(4, station.getLatitude());
                    }

                    @Override
                    public int getBatchSize() {
                        return stations.size();
                    }
                });
    }


    /**
     * Adds the given TripData collection to the TRIP Table {@see se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection#createTRIPTable(java.sql.Connection)}.
     *
     * @param tripCollection parsed trip data
     */
    private void addTripsPrivate(Collection<Trip> tripCollection) {
        final List<Trip> trips = new ArrayList<Trip>(tripCollection);
        template.batchUpdate(PSQLConnection.INSERT_TRIP_STATEMENT, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Trip trip = trips.get(i);
                ps.setLong(1, trip.getBike().getBikeId());
                ps.setTimestamp(2, new Timestamp(trip.getStartTime().getTime()));
                ps.setTimestamp(3, new java.sql.Timestamp(trip.getEndTime().getTime()));
                ps.setLong(4, trip.getDuration());
                ps.setLong(5, trip.getStartsAt().getStationId());
                ps.setLong(6, trip.getEndsAt().getStationId());
                ps.setString(7, trip.getUserType());
                short birthYear = trip.getUserBirthYear();
                if (birthYear == 0)
                    ps.setNull(8, Types.INTEGER);
                else
                    ps.setInt(8, birthYear);
                ps.setInt(9, trip.getUserGender());
            }

            @Override
            public int getBatchSize() {
                return trips.size();
            }
        });
    }
}

