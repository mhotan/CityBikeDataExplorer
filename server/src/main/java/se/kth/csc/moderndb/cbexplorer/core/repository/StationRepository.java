package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import se.kth.csc.moderndb.cbexplorer.domain.Station;

/**
 * Created by mhotan on 4/21/14.
 */
public interface StationRepository extends GraphRepository<Station> {

}
