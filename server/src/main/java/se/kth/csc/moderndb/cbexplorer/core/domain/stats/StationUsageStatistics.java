package se.kth.csc.moderndb.cbexplorer.core.domain.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by mhotan on 5/12/14.
 */
public class StationUsageStatistics {

    private long stationId;

    /**
     * Mappings from stations to the corresponding count of trips
     */
    private Map<Long, Long> departingTripCounts, arrivingTripCounts;

    /**
     * Quick reference to the total counts of arriving and departing trips to this station.
     */
    private long totalDepartingTrips, totalArrivingTrips;

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
                                  Map<Long, Long> departingTripCounts,
                                  Map<Long, Long> arrivingTripCounts) {
        this.stationId = stationId;
        this.departingTripCounts = new HashMap<Long, Long>(departingTripCounts);
        this.arrivingTripCounts = new HashMap<Long, Long>(arrivingTripCounts);

        this.totalDepartingTrips = 0;
        // Do a quick count of the total trips in each direction.
        // This way we only ever calculate it once.
        this.departingTripCounts.values().forEach(new Consumer<Long>() {
            @Override
            public void accept(Long tripCount) {
                totalDepartingTrips += tripCount;
            }
        });
        this.totalArrivingTrips = 0;
        this.arrivingTripCounts.values().forEach(new Consumer<Long>() {
            @Override
            public void accept(Long tripCount) {
                totalArrivingTrips += tripCount;
            }
        });
    }

    /**
     * @return Arriving trip count
     */
    public Map<Long, Long> getArrivingTripCounts() {
        return new HashMap<Long, Long>(arrivingTripCounts);
    }

    /**
     * @return Departing trip count.
     */
    public Map<Long, Long> getDepartingTripCounts() {
        return new HashMap<Long, Long>(departingTripCounts);
    }

    public long getStationId() {
        return stationId;
    }
}
