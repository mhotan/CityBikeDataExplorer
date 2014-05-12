package se.kth.csc.moderndb.cbexplorer.core.domain.range;

/**
 * Created by mhotan on 5/12/14.
 */
public class IntegerRange {

    private int min, max;

    public IntegerRange() {
    }

    public IntegerRange(int min, int max) {
        if (max < min)
            throw new IllegalArgumentException("Maximum int " + max + " cannot " +
                    "be less then min short " + min);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
