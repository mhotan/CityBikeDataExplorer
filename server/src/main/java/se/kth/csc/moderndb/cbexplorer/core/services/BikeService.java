package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.core.events.bikes.AllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.BikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestAllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestBikeEvent;

/**
 * Created by mhotan on 5/7/14.
 */
public interface BikeService {

    public AllBikesEvent requestAllBikes(RequestAllBikesEvent event);

    public BikeEvent requestBike(RequestBikeEvent event);

}
