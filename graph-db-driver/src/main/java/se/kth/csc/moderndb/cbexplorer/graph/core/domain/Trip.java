package se.kth.csc.moderndb.cbexplorer.graph.core.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;

import java.util.Date;

/**
 * Class that represents a Trip between two stations.
 *
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
@TypeAlias(DatabaseConstants.TRIP_LABEL)
public class Trip extends AbstractEntity {

    // Attributes

    // I could not find a time representation in Neo4j
    // This time where the
    Long startTime;

    Long endTime;

    int duration;

    // TODO later figure ways to incorporate Enums with Spring framework.
    // TODO Have to figure out how to ensure that the startTime + startsAt + bike is the primary key.

    String userType;
    Short userBirthYear;
    Short userGender;

    // Relations

    @Fetch
    @RelatedTo(type = DatabaseConstants.STARTS_AT_RELATION, direction = Direction.OUTGOING)
    Station startsAt;

    @Fetch
    @RelatedTo(type = DatabaseConstants.ENDS_AT_RELATION, direction = Direction.OUTGOING)
    Station endsAt;

    @Fetch
    @RelatedTo(type = DatabaseConstants.USES_RELATION, direction = Direction.OUTGOING)
    se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike bike;

    /**
     * Constructor for Neo4J
     */
    public Trip() {}

    /**
     * Creates a new trip.  Depending on the
     *
     * @param startTime The time the trip began
     * @param bike Bike used on the trip
     * @param startsAt Station where the bike started from.
     */
    public Trip(Station startsAt, Bike bike, Date startTime) {
        if (startsAt == null)
            throw new NullPointerException(getClass().getSimpleName() + "() startsAt cannot be null");
        if (bike == null)
            throw new NullPointerException(getClass().getSimpleName() + "() bike cannot be null");
        if (startTime == null)
            throw new NullPointerException(getClass().getSimpleName() + "() startTime cannot be null");

        this.startsAt = startsAt;
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

    public void setStartsAt(Station startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(Station endsAt) {
        this.endsAt = endsAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public Station getStartsAt() {
        return startsAt;
    }

    public Station getEndsAt() {
        return endsAt;
    }

    public se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike getBike() {
        return bike;
    }

    public se.kth.csc.moderndb.cbexplorer.core.data.Trip toCoreTrip() {
        return new se.kth.csc.moderndb.cbexplorer.core.data.Trip(
                new Date(getStartTime()),
                new Date(getEndTime()),
                getDuration(),
                getUserType(),
                getUserBirthYear(),
                getUserGender(),
                getStartsAt().toCoreStation(),
                getEndsAt().toCoreStation(),
                getBike().toCoreBike()
        );
    }

    @Override
    public String toString() {
        return "Trip{" +
                "startTime=" + startTime +
                ", bike=" + bike +
                ", startsAt=" + startsAt +
                '}';
    }
}
