package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.TripReader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

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

    public void createTrip(Trip trip, Station startStation, Station endStation, Bike bike) {
        trip.startedFrom = startStation;
        trip.endedAt = endStation;
        trip.bike = bike;
        template.save(trip);
    }

    public Bike findBike(long id) {
        return template.findUniqueEntity(Bike.class, "id", id);
    }

    public void createBike(Bike bike) {
        template.save(bike);
    }

    public Station findStation(long stationId) {
        return template.findUniqueEntity(Station.class, "stationId", stationId);
    }

    public void createStation(Station station) {
        template.save(station);
    }
}
