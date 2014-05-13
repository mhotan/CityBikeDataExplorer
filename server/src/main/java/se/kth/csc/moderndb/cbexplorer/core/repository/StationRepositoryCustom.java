package se.kth.csc.moderndb.cbexplorer.core.repository;

import java.util.Map;

/**
 * Created by corey on 5/9/14.
 */
public interface StationRepositoryCustom {

    /**
     * Returns the count of trips to stations from a given Station graph Id.
     *
     * @param graphId Graph Id of the Node
     * @return A mapping of station id to the number of trips to those stationId from graphId
     */
    Map<Long, Long> getDestinationCounts(Long graphId);

    /**
     * Returns the number of trips from all other stations to argument station.
     *
     * @param graphId Graph Id of the Node
     * @return Mapping of source station id's to the number of trips.
     */
    Map<Long, Long> getArrivaleCounts(Long graphId);

}
