package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Bike Station entity for graph representation within Neo4j.
 *
 * @author Michael Hotan
 */
@NodeEntity
@TypeAlias(DatabaseConstants.STATION_LABEL)
public class Station extends AbstractEntity {

    /*
    * TODO: Implement pragmatic programming practices once we figure out this works
    * This is very similar to online example.
    * */

    @Indexed(numeric = true, unique = true)
    long stationID;

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
     */
    public Station(long stationID, String name, double longitude, double latitude) {
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

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + getId() +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
