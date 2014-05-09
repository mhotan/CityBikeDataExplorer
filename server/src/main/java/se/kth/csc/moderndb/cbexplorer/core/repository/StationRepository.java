package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

import java.util.List;

import static se.kth.csc.moderndb.cbexplorer.DatabaseConstants.*;

/**
 * Created by mhotan on 4/21/14.
 */
public interface StationRepository extends GraphRepository<Station> {

    Station findByName(@Param("0") String name);

    @Query("START start=node({station}) MATCH start<-[:" + STARTS_AT_RELATION + "]-" +
            "trip-[:" + ENDS_AT_RELATION + "]->end RETURN DISTINCT end")
    List<Station> getDestinations(@Param("station") Long startStationId);

}
