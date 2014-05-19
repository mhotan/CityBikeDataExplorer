package se.kth.csc.moderndb.cbexplorer.core.range;

import java.util.Date;

/**
 * POJO Class that represents Time Range.
 *
 * Created by mhotan on 5/12/14.
 */
public class TimeRange {

    private Date min, max;

    public TimeRange() {
    }

    /**
     * Create a time range argument.
     *
     * @param min Min range.
     * @param max Max range.
     */
    public TimeRange(Date min, Date max) {
        if (min == null || max == null)
            throw new NullPointerException(TimeRange.class.getSimpleName() +
                    "Cant have a null range");
        if (max.getTime() < min.getTime())
            throw new IllegalArgumentException("Maximum date " + max.toString() + " cannot " +
                    "be before " + min.toString());
        this.min = min;
        this.max = max;
    }

    public Date getMin() { return min;}

    public Date getMax() {
        return max;
    }
}
