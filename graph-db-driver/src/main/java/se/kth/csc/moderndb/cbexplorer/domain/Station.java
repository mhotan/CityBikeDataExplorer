package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

/**
 * Bike Station entity for graph representation within Neo4j.
 *
 * @author Michael Hotan
 */
@NodeEntity
public class Station {

    /*
    * TODO: Implement pragmatic programming practices once we figure out this works
    * This is very similar to online example.
    * */

    // Neo4j id reference
    @GraphId Long id;

    // Actual station ID.
    // TODO Verify if these indexes are correct.
    @Indexed(numeric = true, unique = true)
    private Short stationId;

    // The readable name of the Station.
    String name;

    private double latitude, longitude;

    // Point representation of the geographical location
    @Indexed(indexType = IndexType.POINT, indexName = "locations")
    String location;

    /**
     * Constructor for Neo4j
     */
    public Station() {}

    /**
     * Creates a station.
     *
     * @param stationId CitiBike specified Station number
     * @param name Readable Name of the station
     * @param longitude Longitude pertaining to the location of this station
     * @param latitude Latitude pertaining to the location of this station
     */
    public Station(Short stationId, String name, double longitude, double latitude) {
        this.stationId = stationId;
        this.name = name;
        setLocation(longitude, latitude);
    }

    private void setLocation(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = String.format("POINT(%f %f)", longitude, latitude).replace(",",".");
    }

    //////////////////////////////////////////////////////////////////
    //// Place Relation methods.
    //////////////////////////////////////////////////////////////////



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        if (Double.compare(station.latitude, latitude) != 0) return false;
        if (Double.compare(station.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(station.name) : station.name != null) return false;
        if (stationId != null ? !stationId.equals(station.stationId) : station.stationId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = stationId != null ? stationId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
