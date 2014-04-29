package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Class that represents a CitiBike Bike entity.
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
public class Bike extends AbstractEntity {

    @Indexed(unique = true)
    Long bikeId;

    public Bike() {}

    public Bike(long id) {
        this.bikeId = id;
    }

    public Long getBikeId() {
        return bikeId;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "bikeId=" + bikeId +
                '}';
    }
}
