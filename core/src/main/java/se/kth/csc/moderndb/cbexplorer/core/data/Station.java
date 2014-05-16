package se.kth.csc.moderndb.cbexplorer.core.data;

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


    private int tripsDeparted, tripsArrived;

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

    public int getTripsDeparted() {
        return tripsDeparted;
    }

    public void setTripsDeparted(int tripsDeparted) {
        this.tripsDeparted = tripsDeparted;
    }

    public int getTripsArrived() {
        return tripsArrived;
    }

    public void setTripsArrived(int tripsArrived) {
        this.tripsArrived = tripsArrived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        if (stationId != station.stationId) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return (int) (stationId ^ (stationId >>> 32));
    }
}
