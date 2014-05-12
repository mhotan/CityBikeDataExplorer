package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.graph.core.domain.Trip;

import java.util.List;

import static se.kth.csc.moderndb.cbexplorer.DatabaseConstants.*;

/**
 * Repository used for pull specific information about Bikes in the current database.
 * <p/>
 * Created by mhotan on 4/21/14.
 */
public interface BikeRepository extends GraphRepository<Bike> {

    @Query("START bike=node({bike}) MATCH bike<-[:" + USES_RELATION + "]-trip RETURN count(*)")
    Long getTripCount(@Param("bike") Long graphId);

    @Query("start n=node({bike}) match n<-[:USES]-trip, trip-[:STARTS_AT]->strt, trip-[:ENDS_AT]->end " +
           "where trip.startTime > {startTime} and trip.endTime < {endTime} return trip, strt, end;")
    List<Trip> getTrips(@Param("bike") Long graphId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

}
