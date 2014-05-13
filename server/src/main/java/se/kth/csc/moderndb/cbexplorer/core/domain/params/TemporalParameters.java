package se.kth.csc.moderndb.cbexplorer.core.domain.params;

import se.kth.csc.moderndb.cbexplorer.core.domain.range.TimeRange;
import se.kth.csc.moderndb.cbexplorer.core.domain.range.IntegerRange;

/**
 * Parameters that allow the ability to bracket a certain range of time.
 *
 * Created by mhotan on 5/12/14.
 */
public class TemporalParameters {

    private TimeRange timeRange;

    private IntegerRange durationRange;

    public TemporalParameters() {
    }

    public TemporalParameters(TimeRange timeRange, IntegerRange durationRange) {
        this.timeRange = timeRange;
        this.durationRange = durationRange;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * Sets the time range.
     *
     * @param timeRange Time range or null if parameter is cleared.
     */
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    /**
     *
     * @return
     */
    public IntegerRange getDurationRange() {
        return durationRange;
    }

    /**
     * Sets the duration range.
     *
     * @param durationRange Trip duration range or null if parameter is to be cleared.
     */
    public void setDurationRange(IntegerRange durationRange) {
        this.durationRange = durationRange;
    }
}
