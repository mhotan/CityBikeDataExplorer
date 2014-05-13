package se.kth.csc.moderndb.cbexplorer.core.domain.stats;

/**
 * Created by mhotan on 5/12/14.
 */
public class StationUsageStatistics {

    private long stationId;

    /**
     * Quick reference to the total counts of arriving and departing trips to this station.
     */
    private int departingTripCount, arrivingTripCount;

    public StationUsageStatistics() {}

    /**
     * Create Usage Statistics for a specific stations.  The map arguments should be a mapping
     * from a station id to a number count.
     *
     * @param stationId Station ID for statistics.
     * @param departingTripCounts Departing trip data.
     * @param arrivingTripCounts Arriving trip data.
     */
    public StationUsageStatistics(long stationId,
                                  int departingTripCounts,
                                  int arrivingTripCounts) {
        this.stationId = stationId;
        this.departingTripCount = departingTripCounts;
        this.arrivingTripCount = arrivingTripCounts;

    }

    public int getDepartingTripCount() {
        return departingTripCount;
    }

    public int getArrivingTripCount() {
        return arrivingTripCount;
    }

    public long getStationId() {
        return stationId;
    }
}
