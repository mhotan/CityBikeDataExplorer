package se.kth.csc.moderndb.cbexplorer.parser;

import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

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
     * Notification of new Trip Data has been found.
     *
     * @param trips Trips that were read im.
     */
    public void addTrips(Collection<TripData> trips);

}
