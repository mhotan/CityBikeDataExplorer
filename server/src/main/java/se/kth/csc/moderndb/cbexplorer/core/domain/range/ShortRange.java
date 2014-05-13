package se.kth.csc.moderndb.cbexplorer.core.domain.range;

/**
 * Created by mhotan on 5/12/14.
 */
public class ShortRange {

    private short min, max;

    public ShortRange() {
    }

    public ShortRange(short min, short max) {
        if (max < min)
            throw new IllegalArgumentException("Maximum short " + max + " cannot " +
                    "be less then min short " + min);
        this.min = min;
        this.max = max;
    }

    public short getMin() {
        return min;
    }

    public short getMax() {
        return max;
    }
}
