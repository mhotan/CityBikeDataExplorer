package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface BikeDAOi {

    @Transactional
    public Bike findBikeByID(long bikeID);

    @Transactional
    public Bike findAllBikes();
}
