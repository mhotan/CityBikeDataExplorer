package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhotan on 5/1/14.
 */
public class CitiBikeBatchReader implements CitiBikeReader {

    public static final Label BIKE_LABEL = DynamicLabel.label(DatabaseConstants.BIKE_LABEL);
    public static final Label BIKE_PRIMARY_LABEL = DynamicLabel.label("_" + DatabaseConstants.BIKE_LABEL);
    public static final Label STATION_LABEL = DynamicLabel.label(DatabaseConstants.STATION_LABEL);
    public static final Label STATION_PRIMARY_LABEL = DynamicLabel.label("_" + DatabaseConstants.STATION_LABEL);
    public static final Label TRIP_LABEL = DynamicLabel.label(DatabaseConstants.TRIP_LABEL);
    public static final Label TRIP_PRIMARY_LABEL = DynamicLabel.label("_" + DatabaseConstants.TRIP_LABEL);

    // Relations
    public static final RelationshipType startedFrom = DynamicRelationshipType.withName(DatabaseConstants.STARTS_AT_RELATION);
    public static final RelationshipType endedAt = DynamicRelationshipType.withName(DatabaseConstants.ENDS_AT_RELATION);
    public static final RelationshipType uses = DynamicRelationshipType.withName(DatabaseConstants.USES_RELATION);

    private final BatchInserter inserter;

    private long tripCount;

    /**
     * Mappings Citibike IDs -> Neo4j Graph IDs
     */
    private final Map<Long, Long> bikeIDs, stationIDs;

    /**
     * Creates a reader
     *
     * @param inserter The Batch inserter that handles the inserting nodes into the database.
     */
    public CitiBikeBatchReader(BatchInserter inserter) {
        if (inserter == null)
            throw new NullPointerException(getClass().getSimpleName() + "() " +
                    "Cannot have null inserter.");
        this.inserter = inserter;

        // TODO Verify runtime index issue.
//        populateIndexes(inserter);

        // Initialize the tripCount of objects
        tripCount = 0;
        bikeIDs = new HashMap<Long, Long>();
        stationIDs = new HashMap<Long, Long>();
    }

    private static void populateIndexes(BatchInserter inserter) {
        inserter.createDeferredSchemaIndex(BIKE_LABEL).on(DatabaseConstants.BIKE_ID).create();
        inserter.createDeferredSchemaIndex(STATION_LABEL).on(DatabaseConstants.STATION_ID).create();
        inserter.createDeferredSchemaIndex(STATION_LABEL).on(DatabaseConstants.STATION_NAME).create();
        inserter.createDeferredSchemaIndex(TRIP_LABEL).on(DatabaseConstants.TRIP_START_TIME).create();
    }

    @Override
    public void addTrips(Collection<Trip> trips) {

        // Extract the bike and Station data.
        for (Trip trip : trips) {
            Bike bike = trip.getBike();
            Station startStation = trip.getStartsAt();
            Station endStation = trip.getEndsAt();

            // Add all the elements to the database
            long bikeGraphID = addBike(bike);
            long startStationGraphID = addStation(startStation);
            long endStationGraphID = addStation(endStation);
            long tripId = addTrip(trip);

            // Add all the relations.
            inserter.createRelationship(tripId, startStationGraphID, startedFrom, null);
            inserter.createRelationship(tripId, endStationGraphID, endedAt, null);
            inserter.createRelationship(tripId, bikeGraphID, uses, null);

        }
        tripCount += trips.size();
        System.out.println(tripCount + " Total trips processed");
    }

    /**
     *
     * @param data
     * @return The Neo4J Graph ID of the newly added Trip
     */
    private long addTrip(Trip data) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(DatabaseConstants.TRIP_START_TIME, data.getStartTime().getTime());
        properties.put(DatabaseConstants.TRIP_END_TIME, data.getEndTime().getTime());
        properties.put(DatabaseConstants.TRIP_USER_TYPE, data.getUserType());
        properties.put(DatabaseConstants.TRIP_USER_BIRTH_YEAR, data.getUserBirthYear());
        properties.put(DatabaseConstants.TRIP_USER_GENDER, data.getUserGender());
        return inserter.createNode(properties, TRIP_LABEL, TRIP_PRIMARY_LABEL);
    }

    /**
     * Adds a Bike to the database if it does not exists already.
     *
     * @param bike The Bike data to add to the Database
     * @return The Neo4J Graph ID of the newly added  Bike
     */
    private long addBike(Bike bike) {
        // Check if the bike exists in the database
        long citiBikeID = bike.getBikeId();
        if (!bikeIDs.containsKey(citiBikeID)) {

            // Populate the properties
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(DatabaseConstants.BIKE_ID, citiBikeID);

            // Insert into the database
            long bikeGraphID = inserter.createNode(properties, BIKE_LABEL, BIKE_PRIMARY_LABEL);

            // Update the mapping
            bikeIDs.put(citiBikeID, bikeGraphID);

            return bikeGraphID;
        } else
            return bikeIDs.get(citiBikeID);
    }

    /**
     * Adds a station if it does not already exists.
     *
     * @param data Station data to add to the graph.
     * @return The Neo4J Graph ID of the newly added Station
     */
    private long addStation(Station data) {
        assert inserter != null: "Inserter is null";
        // Add the start station if it does not exists already.
        long stationID = data.getStationId();
        if (!stationIDs.containsKey(stationID)) {

            // Populate the properties
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(DatabaseConstants.STATION_NAME, data.getName());
            properties.put(DatabaseConstants.STATION_LONGITUDE, data.getLongitude());
            properties.put(DatabaseConstants.STATION_LATITUDE, data.getLatitude());
            properties.put(DatabaseConstants.STATION_ID, stationID);

            // Insert into the database
            long stationGraphID = inserter.createNode(properties, STATION_LABEL, STATION_PRIMARY_LABEL);

            // Update the mapping
            stationIDs.put(stationID, stationGraphID);

            // Update the mapping
            return stationGraphID;
        } else {
            return stationIDs.get(stationID);
        }
    }
}
