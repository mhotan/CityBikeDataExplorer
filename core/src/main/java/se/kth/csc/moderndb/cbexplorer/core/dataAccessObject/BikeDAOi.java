package se.kth.csc.moderndb.cbexplorer.core.dataAccessObject;

import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;

import java.util.List;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface BikeDAOi {

    public Bike findBikeByID(long bikeID);
    public Bike findAllBikes();
}
