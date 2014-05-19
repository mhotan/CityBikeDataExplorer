package se.kth.csc.moderndb.cbexplorer.core.repository;

import se.kth.csc.moderndb.cbexplorer.core.data.Station;

import java.util.List;

/**
 * Interface for the Data Access Object pattern for the Station Table.
 *
 * Created by Jeannine on 07.05.14.
 */
public interface StationDAOi {

    public List<Station> findStationByID(long stationId) throws Exception;

    public List<Station> findAllStations();

    public List<Station> findStationByName(String name);

    public List<List<Station>> findStationPairsWithDistance(double distance);

    Double findDistanceBtwStations(long station_id1, long station_id2);
}
