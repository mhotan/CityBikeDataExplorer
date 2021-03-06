package se.kth.csc.moderndb.cbexplorer.core.services;

import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.core.domain.Trip;
import se.kth.csc.moderndb.cbexplorer.core.repository.BikeRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.StationRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.TripRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    /**
     * Mapping to station id to its corresponding statistics.
     */
//    private Map<Station, StationUsageStatistics> stationStatistics;

    public GraphServiceImpl(BikeRepository bikeRepository,
                            TripRepository tripRepository,
                            final StationRepository stationRepository) {
        if (bikeRepository == null || tripRepository == null || stationRepository == null)
            throw new NullPointerException("Can't have null repositories");
        this.bikeRepository = bikeRepository;
        this.tripRepository = tripRepository;
        this.stationRepository = stationRepository;

    }

//    /**
//     * @return The station statistics for all the stations.
//     */
//    @Transactional
//    private Map<Station, StationUsageStatistics> getStationStats() {
//        final Map<Station, StationUsageStatistics> stats = new HashMap<Station, StationUsageStatistics>();
//        DstationRepository.findAll().forEach(new Consumer<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station>() {
//            @Override
//            public void accept(se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station station) {
//                Map<Long, Long> departingCounts = stationRepository.getDestinationCounts(station.getId());
//                Map<Long, Long> arrivalCounts = stationRepository.getArrivaleCounts(station.getId());
//                stats.put(station.toCoreStation(), new StationUsageStatistics(
//                        station.getStationId(), departingCounts, arrivalCounts));
//            }
//        });
//        return stats;
//    }

    @Override
    public List<Bike> findAllBikes() {
        final List<Bike> bikes = new ArrayList<Bike>();
        bikeRepository.findAll().forEach(new Consumer<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike>() {
            @Override
            public void accept(se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike bike) {
                bikes.add(bike.toCoreBike());
            }
        });
        return bikes;
    }

    @Override
    public Bike findBike(long bikeID) {
        return bikeRepository.findBySchemaPropertyValue(DatabaseConstants.BIKE_ID, bikeID).toCoreBike();
    }

    @Override
    public Long getBikeTripCount(long bikeID) {
        se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike bike = bikeRepository.findBySchemaPropertyValue(
                DatabaseConstants.BIKE_ID, bikeID);
        if (bike == null) {
            return null;
        }
        return bikeRepository.getTripCount(bike.getId());
    }

    @Override
    public List<Trip> getBikeTrips(long bikeID, long startTime, long endTime) {
        se.kth.csc.moderndb.cbexplorer.graph.core.domain.Bike bike = bikeRepository.findBySchemaPropertyValue(DatabaseConstants.BIKE_ID, bikeID);
        if (bike == null) {
            return null;
        }
        return tripsToCoreList(bikeRepository.getTrips(bike.getId(), startTime, endTime));
    }

    @Override
    public List<Station> findAllStations() {
        return stationsToCoreList(stationRepository.findAll());
    }

    @Override
    public Station findStation(long stationId) {
        return stationRepository.findBySchemaPropertyValue(DatabaseConstants.STATION_ID, stationId).toCoreStation();
    }

    @Override
    public Station findStationByName(String name) {
        return stationRepository.findByName(name).toCoreStation();
    }

    @Override
    public Map<Long, Long> getStationDestinations(long startStationId) {
        se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station station = stationRepository.findBySchemaPropertyValue(DatabaseConstants.STATION_ID, startStationId);
        if (station == null) {
            return null;
        }
        final Map<Long, Long> stations = new HashMap<Long, Long>();
        stationRepository.getDestinationCounts(station.getId()).forEach(new BiConsumer<Long, Long>() {
            @Override
            public void accept(Long stationId, Long aLong) {
                stations.put(stationId, aLong);
            }
        });
        return stations;
    }

    private List<Station> stationsToCoreList(Iterable<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station> result) {
        final List<Station> stations = new ArrayList<Station>();
        result.forEach(new Consumer<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station>() {
            @Override
            public void accept(se.kth.csc.moderndb.cbexplorer.graph.core.domain.Station station) {
                stations.add(station.toCoreStation());
            }
        });
        return stations;
    }

    private List<Trip> tripsToCoreList(Iterable<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Trip> result) {
        final List<Trip> trips = new ArrayList<Trip>();
        result.forEach(new Consumer<se.kth.csc.moderndb.cbexplorer.graph.core.domain.Trip>() {
            @Override
            public void accept(se.kth.csc.moderndb.cbexplorer.graph.core.domain.Trip trip) {
                trips.add(trip.toCoreTrip());
            }
        });
        return trips;
    }

//    @Override
//    public StationUsageStatistics findStationStatistics(long stationId) {
////        for (Station station: stationStatistics.keySet()) {
////            if (station.getStationId() == stationId)
////                return stationStatistics.get(station);
////        }
////        TODO
//        return null;
//    }
//
//    @Override
//    public Map<Long, StationUsageStatistics> findAllStationStatistics() {
//        // Make sure we have the stations
//        if (stationStatistics == null) {
//            stationStatistics = getStationStats();
//        }
//
//        // Populate the stats.
//        final Map<Long, StationUsageStatistics> stats = new HashMap<Long, StationUsageStatistics>();
//        stationStatistics.keySet().forEach(new Consumer<Station>() {
//            @Override
//            public void accept(Station station) {
//                stats.put(station.getStationId(), stationStatistics.get(station));
//            }
//        });
//        return stats;
//
//    }
}
