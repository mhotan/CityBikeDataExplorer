package se.kth.csc.moderndb.cbexplorer.parser;

import org.springframework.beans.factory.annotation.Autowired;
import se.kth.csc.moderndb.cbexplorer.domain.*;
import se.kth.csc.moderndb.cbexplorer.parser.data.*;

import java.util.Collection;

/**
 * Receives trip data from the parser and saves it to Neo4j.
 */
public class TripReader implements CitiBikeReader {
    @Autowired
    private CitiBikeGraphService graphService;

    @Override
    public void addTrips(Collection<TripData> trips) {
        for (TripData tripData : trips) {
            Trip trip = new Trip(tripData.getStartTime(), tripData.getEndTime(), tripData.getUserType(),
                    tripData.getUserBirthYear(), tripData.getUserGenderAsString());
            Bike bike = obtainBike(tripData.getBikeData());
            Station startStation = obtainStation(tripData.getStartStationData());
            Station endStation = obtainStation(tripData.getEndStationData());

            graphService.createTrip(trip, startStation, endStation, bike);
        }
    }

    /**
     * Find or create a Bike instance corresponding to the data.
     *
     * @param bikeData
     * @return new or existing Bike with a matching id
     */
    private Bike obtainBike(BikeData bikeData) {
        Bike bike = graphService.findBike(bikeData.getId());
        if (bike == null) {
            bike = new Bike(bikeData.getId());
            graphService.createBike(bike);
        }
        return bike;
    }

    /**
     * Find or create a Station instance corresponding to the data.
     *
     * @param stationData
     * @return new or existing Station with a matching stationId
     */
    private Station obtainStation(StationData stationData) {
        Station station = graphService.findStation(stationData.getStationId());
        if (station == null) {
            station = new Station(stationData.getStationId(), stationData.getName(),
                    stationData.getLongitude(), stationData.getLatitude());
            graphService.createStation(station);
        }
        return station;
    }
}
