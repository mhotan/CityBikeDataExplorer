package se.kth.csc.moderndb.cbexplorer.core.events.bikes;

import se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.events.ReadEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by mhotan on 5/7/14.
 */
public class AllBikesEvent extends ReadEvent {

    private final List<Bike> bikes;

    public AllBikesEvent(List<Bike> bikes) {
        this.bikes = Collections.unmodifiableList(bikes);
    }

    public Collection<Bike> getBikes() {
        return bikes;
    }
}
