package se.kth.csc.moderndb.cbexplorer.core.domain;

/**
 * Station POJO class for REST Service.
 *
 * Created by mhotan on 5/7/14.
 */
public class Station {

    /**
     * Unique Citibike generated ID.
     */
    private long stationId;

    /**
     * Name of the Station.
     */
    private String name;

    /**
     * Location of the station.
     */
    private double longitude, latitude;

    /**
     * Creates base POJO object for a Station.
     */
    public Station() {}

    /**
     * Creates a Station with all necessary data.
     *
     * @param stationId Unique Station Id of this Station.
     * @param name Readable name for this station.
     * @param longitude Longitude of the station's location at SRID 4326
     * @param latitude Latitude of the station's location at SRID 4326
     */
    public Station(long stationId, String name, double longitude, double latitude) {
        this.stationId = stationId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
