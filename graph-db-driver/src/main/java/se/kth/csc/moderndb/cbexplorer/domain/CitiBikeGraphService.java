package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;
import se.kth.csc.moderndb.cbexplorer.parser.data.BikeData;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

/**
 * Created by mhotan on 4/13/14.
 */
public class CitiBikeGraphService {
    @Autowired
    private Neo4jTemplate template;

    @Transactional
    public void importTrips(File file) throws IOException, ParseException {
        new CitiBikeParser(new TripReader()).parse(file);
    }

    /**
     * Receives trip data from the parser and saves it to Neo4j.
     */
    public class TripReader implements CitiBikeReader {
        @Override
        public void addTrips(Collection<TripData> trips) {
            for (TripData tripData : trips) {
                Trip trip = new Trip(tripData.getStartTime(), tripData.getEndTime(), tripData.getUserType(),
                        tripData.getUserBirthYear(), tripData.getUserGenderAsString());
                Bike bike = obtainBike(tripData.getBikeData());
                Station startStation = obtainStation(tripData.getStartStationData());
                Station endStation = obtainStation(tripData.getEndStationData());

                trip.startedFrom = startStation;
                trip.endedAt = endStation;
                trip.bike = bike;
                template.save(trip);
            }
        }

        /**
         * Find or create a Bike instance corresponding to the data.
         * @param bikeData
         * @return new or existing Bike with a matching id
         */
        private Bike obtainBike(BikeData bikeData) {
            Bike bike = template.findUniqueEntity(Bike.class, "id", bikeData.getId());
            if (bike == null) {
                bike = new Bike(bikeData.getId());
                template.save(bike);
            }
            return bike;
        }

        /**
         * Find or create a Station instance corresponding to the data.
         * @param stationData
         * @return new or existing Station with a matching stationId
         */
        private Station obtainStation(StationData stationData) {
            Station station = template.findUniqueEntity(Station.class, "stationId", stationData.getStationId());
            if (station == null) {
                station = new Station(stationData.getStationId(), stationData.getName(),
                        stationData.getLongitude(), stationData.getLatitude());
                template.save(station);
            }
            return station;
        }
    }
}
