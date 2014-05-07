package se.kth.csc.moderndb.cbexplorer.data;

import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;

/**
 * Created by Jeannine on 05.05.14.
 */
public class TripRoute {

    private long id;
    private StationData start;
    private StationData end;

    public TripRoute(long id, StationData start, StationData end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public StationData getStart() {
        return start;
    }

    public StationData getEnd() {
        return end;
    }
}
