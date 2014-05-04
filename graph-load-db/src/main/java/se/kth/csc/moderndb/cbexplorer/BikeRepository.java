package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Repository used for pull specific information about Bikes in the current database.
 *
 * Created by mhotan on 4/21/14.
 */
public interface BikeRepository extends GraphRepository<Bike> {

}
