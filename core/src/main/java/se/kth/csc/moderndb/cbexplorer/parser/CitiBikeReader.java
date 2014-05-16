package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.core.data.Trip;

import java.util.Collection;

/**
 * Reads Citibike data from the {@link se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser}.
 * If you want to read data from some sort of data source implement this interface and use a
 * trip data parser or provider like {@link se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser}.
 *
 * Created by mhotan on 4/13/14.
 */
public interface CitiBikeReader {

    /**
     * Notifies any implementing class that all the trips, stations, and bikes have been
     *  processed from the data file.  The Trip data object contains the stations and the bikes.
     *
     * @param trips All the trips to add.
     */
    public void addTrips(Collection<Trip> trips);

}
