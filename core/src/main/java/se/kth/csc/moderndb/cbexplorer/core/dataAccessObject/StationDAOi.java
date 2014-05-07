package se.kth.csc.moderndb.cbexplorer.core.dataAccessObject;

import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface StationDAOi {

    public Station findStationByID(long stationId);
}
