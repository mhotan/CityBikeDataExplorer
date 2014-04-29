package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Bike Station entity for graph representation within Neo4j.
 *
 * @author Michael Hotan
 */
@NodeEntity
public class Station extends AbstractEntity {

    /*
    * TODO: Implement pragmatic programming practices once we figure out this works
    * This is very similar to online example.
    * */

    // Actual station ID.
    // TODO Verify if these indexes are correct.
    @Indexed(numeric = true, unique = true)
    private Long stationId;

    // The readable name of the Station.
    String name;

    private double latitude, longitude;

    // Point representation of the geographical location
    String location;

    /**
     * Constructor for Neo4j
     */
    public Station() {}

    /**
     * Creates a station.
     *
     * @param stationId CitiBike specified Station number
     */
    public Station(long stationId, String name, double longitude, double latitude) {
        this.stationId = stationId;
        setName(name);
        setLocation(longitude, latitude);
    }

    /**
     * Set the name of this Station.
     *
     * @param name The name to set for this Station.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the position of this station.
     *
     * @param longitude
     * @param latitude
     */
    private void setLocation(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = String.format("POINT(%f %f)", longitude, latitude).replace(",",".");
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + stationId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
