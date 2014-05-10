package se.kth.csc.moderndb.cbexplorer.core.services;

import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

import java.util.List;

/**
 * Basic service that handles Graph like queries.
 *
 * Created by mhotan on 5/9/14.
 */
public interface GraphService {

    /**
     * @return All the Request all the bikes available
     */
    @Transactional
    public List<Bike> findAllBikes();

    /**
     *
     *
     * @param bikeID Returns a Bike.
     * @return Bike with the BikeID.
     */
    @Transactional
    public Bike findBike(long bikeID);

    @Transactional
    public Long getBikeTripCount(long bikeID);

    // TODO Place the base requirements for any graph related request.
    // Be sure to add @Transactional.

    @Transactional
    public List<Station> findAllStations();

    @Transactional
    public Station findStation(long stationId);

    @Transactional
    public Station findStationByName(String name);

    @Transactional
    public List<Station> getStationDestinations(long startStationId);
}
