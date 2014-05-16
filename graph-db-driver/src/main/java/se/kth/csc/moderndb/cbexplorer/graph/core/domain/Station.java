package se.kth.csc.moderndb.cbexplorer.graph.core.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;

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
    long stationId;

    // The readable name of the Station.
    String name;

    private double latitude, longitude;

    /**
     * Constructor for Neo4j
     */
    public Station() {}

    /**
     * Creates a station.
     *
     */
    public Station(long stationId, String name, double longitude, double latitude) {
        this.stationId = stationId;
        setName(name);
        setLocation(longitude, latitude);
    }

    public se.kth.csc.moderndb.cbexplorer.core.data.Station toCoreStation() {
        return new se.kth.csc.moderndb.cbexplorer.core.data.Station(stationId, name, longitude, latitude);
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
    }

    public long getStationId() {
        return stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
