package se.kth.csc.moderndb.cbexplorer.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;
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
                Bike bike; // TODO find or create these nodes as necessary
                Station startStation, endStation;
                template.save(trip);
            }
        }
    }
}
