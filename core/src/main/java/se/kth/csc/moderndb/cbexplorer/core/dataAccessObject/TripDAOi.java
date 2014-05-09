package se.kth.csc.moderndb.cbexplorer.core.dataAccessObject;

import se.kth.csc.moderndb.cbexplorer.core.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.core.domain.TripParameters;
import se.kth.csc.moderndb.cbexplorer.core.domain.UserParameters;

import java.util.Date;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface TripDAOi {

    public Trip findTripByID(long bikeID, Date startDate);

    public Trip findTripSpecifiedByUserCharacteristics(UserParameters userParameters);


}
