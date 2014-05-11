package se.kth.csc.moderndb.cbexplorer.core.repository;

import java.util.Map;

/**
 * Created by corey on 5/9/14.
 */
public interface StationRepositoryCustom {
    Map<Long, Long> getDestinationCounts(Long graphId);
}
