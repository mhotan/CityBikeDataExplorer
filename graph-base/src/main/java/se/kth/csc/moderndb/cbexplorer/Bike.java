package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Class that represents a CitiBike Bike entity.
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
@TypeAlias(DatabaseConstants.BIKE_LABEL)
public class Bike extends AbstractEntity {

    @Indexed(numeric = true, unique = true)
    private long bikeID;

    public Bike() {
    }

    public Bike(long bikeID) {
        this.bikeID = bikeID;
    }

    public long getBikeID() {
        return bikeID;
    }
}
