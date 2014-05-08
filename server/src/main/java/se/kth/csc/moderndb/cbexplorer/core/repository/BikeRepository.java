package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import se.kth.csc.moderndb.cbexplorer.domain.Bike;

import static se.kth.csc.moderndb.cbexplorer.DatabaseConstants.*;

/**
 * Repository used for pull specific information about Bikes in the current database.
 * <p/>
 * Created by mhotan on 4/21/14.
 */
public interface BikeRepository extends GraphRepository<Bike> {

    @Query("START bike=node({bike}) MATCH bike<-[:" + USES_RELATION + "]-trip RETURN count(*)")
    Long getTripCount(@Param("bike") Long graphId);

}
