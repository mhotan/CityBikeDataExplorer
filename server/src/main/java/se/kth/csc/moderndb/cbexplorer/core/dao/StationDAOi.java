package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

/**
 * Interface for the Data Access Object pattern.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface StationDAOi {

    @Transactional
    public Station findStationByID(long stationId);

    @Transactional
    public Station findAllStations();

    @Transactional
    public Station findStationByName(String name);

    @Transactional
    Double findDistanceBtwStations(long station_id1, long station_id2);
}
