package se.kth.csc.moderndb.cbexplorer.core.domain.params;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhotan on 5/12/14.
 */
public class BikeParameters {

    private List<Long> selectedBikes;

    public BikeParameters(List<Long> selectedBikes) {
        if (selectedBikes == null)
            this.selectedBikes = null;
        this.selectedBikes = new ArrayList<Long>(selectedBikes);
    }

    public BikeParameters() {
    }

    /**
     * @return Mutable copy of selected bikes
     */
    public List<Long> getSelectedBikes() {
        return new ArrayList<Long>(selectedBikes);
    }

    public void addSelectedBike(long bikeId) {
        selectedBikes.add(bikeId);
    }

    public boolean removeSelectedBike(long bikeId) {
        return selectedBikes.remove(bikeId);
    }
}
