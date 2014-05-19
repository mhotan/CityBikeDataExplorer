package se.kth.csc.moderndb.cbexplorer.core.params;

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

    /**
     * Adds a bike Id to the selected bikes
     * @param bikeId The bike to be selected.
     */
    public void addSelectedBike(long bikeId) {
        if (this.selectedBikes.contains(bikeId)) return;
        selectedBikes.add(bikeId);
    }

    /**
     *
     * @param bikeId The id of the bike to be removed
     * @return
     */
    public boolean removeSelectedBike(long bikeId) {
        return selectedBikes.remove(bikeId);
    }
}
