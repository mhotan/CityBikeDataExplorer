package se.kth.csc.moderndb.cbexplorer.reader;

import se.kth.csc.moderndb.cbexplorer.reader.data.TripData;

import java.util.Collection;

/**
 * Reads Citibike data from the {@link se.kth.csc.moderndb.cbexplorer.reader.CitiBikeParser}.
 * If you want to read data from some sort of data source implement this interface and use a
 * trip data reader or provider like {@link se.kth.csc.moderndb.cbexplorer.reader.CitiBikeParser}.
 *
 * Created by mhotan on 4/13/14.
 */
public interface CitiBikeReader {

    /**
     * Notification of new Trip Data has been found.
     *
     * @param trips Trips that were read in.
     */
    public void addTrips(Collection<TripData> trips);

}
