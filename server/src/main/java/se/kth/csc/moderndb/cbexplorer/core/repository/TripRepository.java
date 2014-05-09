package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import se.kth.csc.moderndb.cbexplorer.graph.core.domain.Trip;

/**
 * Created by mhotan on 4/21/14.
 */
public interface TripRepository extends GraphRepository<Trip> {

}
