package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.repository.BikeRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.StationRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.TripRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link se.kth.csc.moderndb.cbexplorer.core.services.GraphService}. Uses provided
 * Graph Repositories.  Delegates database interaction to the internal Graph Repositories.
 *
 * Created by mhotan on 5/9/14.
 */
public class GraphServiceImpl implements GraphService {

    private final BikeRepository bikeRepository;

    private final TripRepository tripRepository;

    private final StationRepository stationRepository;

    public GraphServiceImpl(BikeRepository bikeRepository,
                            TripRepository tripRepository,
                            StationRepository stationRepository) {
        if (bikeRepository == null || tripRepository == null || stationRepository == null)
            throw new NullPointerException("Can't have null repositories");
        this.bikeRepository = bikeRepository;
        this.tripRepository = tripRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public List<Bike> requestAllBikes() {
        List<Bike> bikes = new ArrayList<Bike>();
        for (Bike bike: bikeRepository.findAll()) {
            bikes.add(bike);
        }
        System.out.println("requestAllBikes(), " + bikes.size() + " bikes found.");
        return bikes;
    }

    @Override
    public Bike requestBike(long bikeID) {
        return bikeRepository.findBySchemaPropertyValue(DatabaseConstants.BIKE_ID, bikeID);
    }
}
