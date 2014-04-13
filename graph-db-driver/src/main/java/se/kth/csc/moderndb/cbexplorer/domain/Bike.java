package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Class that represents a CitiBike Bike entity.
 * Created by mhotan on 4/13/14.
 */
@NodeEntity
public class Bike {

    @Indexed(indexName = "bikeIDs", unique = true)
    Long id;

    public Bike() {}

    public Bike(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike)) return false;

        Bike bike = (Bike) o;
        if (!id.equals(bike.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
