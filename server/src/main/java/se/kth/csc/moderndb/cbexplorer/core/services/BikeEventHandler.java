package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.AllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.BikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestAllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestBikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.repository.BikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by mhotan on 5/7/14.
 */
public class BikeEventHandler implements BikeService {

    private final BikeRepository bikeRepository;

    public BikeEventHandler(BikeRepository bikeRepository) {
        if (bikeRepository == null)
            throw new NullPointerException("Bike repo cannot be null");
        this.bikeRepository = bikeRepository;
    }

    @Override
    public AllBikesEvent requestAllBikes(RequestAllBikesEvent event) {
        final List<Bike> bikes = new ArrayList<Bike>();
        this.bikeRepository.findAll().forEach(new Consumer<se.kth.csc.moderndb.cbexplorer.domain.Bike>() {
            @Override
            public void accept(se.kth.csc.moderndb.cbexplorer.domain.Bike bike) {
                // Translate the graph bike to POJO rest bike.
                bikes.add(new Bike(bike.getBikeId()));
            }
        });
        return new AllBikesEvent(bikes);
    }

    @Override
    public BikeEvent requestBike(RequestBikeEvent event) {
        se.kth.csc.moderndb.cbexplorer.domain.Bike bike =
                this.bikeRepository.findBySchemaPropertyValue(DatabaseConstants.BIKE_ID, event.getBikeId());
        if (bike == null)
            return BikeEvent.notFound(event.getBikeId());
        return new BikeEvent(new Bike(bike.getBikeId()));
    }
}
