package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;

import java.util.List;

/**
 * Basic service that handles Graph like queries.
 *
 * Created by mhotan on 5/9/14.
 */
public interface GraphService {

    public List<Bike> requestAllBikes();

    public Bike requestBike(long bikeID);

    // TODO Place the base requirements for any graph related request.


}
