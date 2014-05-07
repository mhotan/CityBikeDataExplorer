package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;

/**
 * Class that represents a CitiBike Bike entity.
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
@TypeAlias(DatabaseConstants.BIKE_LABEL)
public class Bike extends AbstractEntity {

    @Indexed(numeric = true, unique = true)
    private long bikeId;

    public Bike() {
    }

    public Bike(long bikeId) {
        this.bikeId = bikeId;
    }

    public long getBikeId() {
        return bikeId;
    }
}
