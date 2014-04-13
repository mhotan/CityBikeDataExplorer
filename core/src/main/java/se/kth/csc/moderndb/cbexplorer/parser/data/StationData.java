package se.kth.csc.moderndb.cbexplorer.parser.data;

import se.kth.csc.moderndb.cbexplorer.exception.RepresentationException;

/**
 * Raw data for a Citibike station.
 *
 * Created by mhotan on 4/13/14.
 */
public class StationData {

    private final long stationId;

    private final String name;

    private final double longitude;

    private final double latitude;

    public StationData(long stationId, String name, double longitude, double latitude) {
        this.stationId = stationId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        checkRep();
    }

    /**
     * @return The ID of the Citibike station
     */
    public long getStationId() {
        return stationId;
    }

    /**
     * @return The Name of the station
     */
    public String getName() {
        return name;
    }

    /**
     * @return The Longitude of the location of this station
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return The Latitude of the location of this station.
     */
    public double getLatitude() {
        return latitude;
    }

    private void checkRep() {
        if (name == null) {
            throw new RepresentationException("Name cannot be nul");
        }
    }
}
