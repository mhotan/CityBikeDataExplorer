package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

import java.util.List;

/**
 * Interface for the Data Access Object pattern for the Station Table.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface StationDAOi {

    @Transactional
    public List<Station> findStationByID(long stationId) throws Exception;

    @Transactional
    public List<Station> findAllStations();

    @Transactional
    public List<Station> findStationByName(String name);

    @Transactional
    public List<List<Station>> findStationPairsWithDistance(double distance);

    @Transactional
    Double findDistanceBtwStations(long station_id1, long station_id2);

}
