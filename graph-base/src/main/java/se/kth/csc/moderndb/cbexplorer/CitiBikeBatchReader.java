package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;
import se.kth.csc.moderndb.cbexplorer.parser.data.BikeData;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhotan on 5/1/14.
 */
public class CitiBikeBatchReader implements CitiBikeReader {

    public static final Label BIKE_LABEL = DynamicLabel.label("Bike");
    public static final Label STATION_LABEL = DynamicLabel.label("Station");
    public static final Label TRIP_LABEL = DynamicLabel.label("Trip");

    // Station
    public static final String STATION_NAME = "name";
    public static final String STATION_LOCATION = "location";

    // Trip
    public static final String TRIP_STARTTIME = "startTime";
    public static final String TRIP_ENDTIME = "endTime";
    public static final String TRIP_USERTYPE = "userType";
    public static final String TRIP_USERBIRTHYEAR = "userBirthYear";
    public static final String TRIP_USERGENDER = "userGender";

    // Relation
    public static final RelationshipType startedFrom = DynamicRelationshipType.withName("STARTED_FROM");
    public static final RelationshipType endedAt = DynamicRelationshipType.withName("ENDED_AT");
    public static final RelationshipType uses = DynamicRelationshipType.withName("USES");

    private final BatchInserter inserter;

    private long tripCount;

    /**
     *
     *
     * @param inserter The Batch inserter that handles the inserting nodes into the database.
     */
    public CitiBikeBatchReader(BatchInserter inserter) {
        if (inserter == null)
            throw new NullPointerException(getClass().getSimpleName() + "() " +
                    "Cannot have null inserter.");
        this.inserter = inserter;

        // Initialize the tripCount of objects
        tripCount = 0;
    }

    private static void populateIndexes(BatchInserter inserter) {
//TODO
    }

    @Override
    public void addTrips(Collection<TripData> trips) {

        // Extract the bike and Station data.
        for (TripData tripData: trips) {
            BikeData bikeData = tripData.getBikeData();
            StationData startStationData = tripData.getStartStationData();
            StationData endStationData = tripData.getEndStationData();

            // Check if the bike exists in the database
            if (!inserter.nodeExists(bikeData.getId())) {
                Map<String, Object> properties = new HashMap<String, Object>();
                inserter.createNode(bikeData.getId(), properties, BIKE_LABEL);
            }

            // Add the start station if it does not exists already.
            if (!inserter.nodeExists(startStationData.getStationId())) {
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put(STATION_NAME, startStationData.getName());
                String location = String.format("POINT(%f %f)",
                        startStationData.getLongitude(), startStationData.getLatitude()).replace(",", ".");
                properties.put(STATION_LOCATION, location);
                inserter.createNode(startStationData.getStationId(), properties, STATION_LABEL);
            }

            // Add the end station if it does not exists already.
            if (!inserter.nodeExists(endStationData.getStationId())) {
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put(STATION_NAME, endStationData.getStationId());
                String location = String.format("POINT(%f %f)",
                        endStationData.getLongitude(), endStationData.getLatitude()).replace(",", ".");
                properties.put(STATION_LOCATION, location);
                inserter.createNode(endStationData.getStationId(), properties, STATION_LABEL);
            }

            // Add the trip.
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(TRIP_STARTTIME, tripData.getStartTime().getTime());
            properties.put(TRIP_ENDTIME, tripData.getEndTime().getTime());
            properties.put(TRIP_USERTYPE, tripData.getUserType());
            properties.put(TRIP_USERBIRTHYEAR, tripData.getUserBirthYear());
            properties.put(TRIP_USERGENDER, tripData.getUserGender());
            long tripId = inserter.createNode(properties, TRIP_LABEL);

            // Add all the relations.
            inserter.createRelationship(tripId, startStationData.getStationId(), startedFrom, null);
            inserter.createRelationship(tripId, endStationData.getStationId(), endedAt, null);
            inserter.createRelationship(tripId, bikeData.getId(), uses, null);

        }
        tripCount += trips.size();
        System.out.println(tripCount + " Total trips processed");
    }
}
