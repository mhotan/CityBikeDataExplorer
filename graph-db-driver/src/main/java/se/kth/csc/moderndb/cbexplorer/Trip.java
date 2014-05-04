package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;

/**
 * Class that represents a Trip between two stations.
 *
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
public class Trip extends AbstractEntity {

    // Relation types
    public static final String STARTED_FROM_TYPE = "STARTED_FROM";
    public static final String ENDED_AT_TYPE = "ENDED_AT";
    public static final String USES_TYPE = "USES";

    // Attributes

    // I could not find a time representation in Neo4j
    // This time where the
    Long startTime;

    Long endTime;

    // TODO later figure ways to incorporate Enums with Spring framework.
    // TODO Have to figure out how to ensure that the startTime + startedFrom + bike is the primary key.

    String userType;
    Short userBirthYear;
    Short userGender;

    // Relations

    @Fetch
    @RelatedTo(type = STARTED_FROM_TYPE, direction = Direction.OUTGOING)
    Station startedFrom;

    @Fetch
    @RelatedTo(type = ENDED_AT_TYPE, direction = Direction.OUTGOING)
    Station endedAt;

    @Fetch
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
     * @param bike Bike used on the trip
     * @param startedFrom Station where the bike started from.
     */
    public Trip(Station startedFrom, Bike bike, Date startTime) {
        if (startedFrom == null)
            throw new NullPointerException(getClass().getSimpleName() + "() startedFrom cannot be null");
        if (bike == null)
            throw new NullPointerException(getClass().getSimpleName() + "() bike cannot be null");
        if (startTime == null)
            throw new NullPointerException(getClass().getSimpleName() + "() startTime cannot be null");

        this.startedFrom = startedFrom;
        this.bike = bike;
        this.startTime = startTime.getTime();
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserBirthYear(Short userBirthYear) {
        this.userBirthYear = userBirthYear;
    }

    public void setUserGender(Short userGender) {
        this.userGender = userGender;
    }

    public void setEndedAt(Station endedAt) {
        this.endedAt = endedAt;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getUserType() {
        return userType;
    }

    public Short getUserBirthYear() {
        return userBirthYear;
    }

    public Short getUserGender() {
        return userGender;
    }

    public Station getStartedFrom() {
        return startedFrom;
    }

    public Station getEndedAt() {
        return endedAt;
    }

    public Bike getBike() {
        return bike;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "startTime=" + startTime +
                ", bike=" + bike +
                ", startedFrom=" + startedFrom +
                '}';
    }
}
