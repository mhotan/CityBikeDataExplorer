package se.kth.csc.moderndb.cbexplorer.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;

/**
 * Class that represents a Trip between two stations.
 *
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
public class Trip {

    // Relation types
    public static final String STARTED_FROM_TYPE = "STARTED_FROM";
    public static final String ENDED_AT_TYPE = "ENDED_AT";
    public static final String USES_TYPE = "USES";

    // Neo4j id reference
    @GraphId Long id;

    // Attributes

    // I could not find a time representation in Neo4j
    // This time where the
    Long startTime;

    Long endTime;

    // TODO later figure ways to incorporate Enums with Spring framework.

    String userType;

    Short userBirthYear;

    String userGender;

    // Relations

    @RelatedTo(type = STARTED_FROM_TYPE, direction = Direction.OUTGOING)
    Station startedFrom;

    @RelatedTo(type = ENDED_AT_TYPE, direction = Direction.OUTGOING)
    Station endedAt;

    @RelatedTo(type = USES_TYPE, direction = Direction.OUTGOING)
    Bike bike;

    /**
     * Constructor for Neo4J
     */
    public Trip() {}

    /**
     * Creates a new trip.  Depending on the
     *
     * @param startTime The time the trip began
     * @param endTime The time the trip ended
     * @param userType Whether the user is a one time customer or member
     * @param userBirthYear Birth year of the user if there is one
     * @param gender Gender of the user if there was one recorded.
     */
    public Trip(Date startTime, Date endTime, String userType, Short userBirthYear, String gender) {
        this.startTime = startTime.getTime();
        this.endTime = endTime.getTime();
        this.userType = userType;
        this.userBirthYear = userBirthYear;
        this.userGender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        if (bike != null ? !bike.equals(trip.bike) : trip.bike != null) return false;
        if (startTime != null ? !startTime.equals(trip.startTime) : trip.startTime != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (bike != null ? bike.hashCode() : 0);
        return result;
    }
}
