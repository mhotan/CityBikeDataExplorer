package se.kth.csc.moderndb.cbexplorer.core.repository;

import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

import java.util.Map;

/**
 * Created by corey on 5/9/14.
 */
public interface StationCustomRepository {
    Map<Station, Long> getDestinationCounts(Long graphId);
}
