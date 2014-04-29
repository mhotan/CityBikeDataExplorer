package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;
import se.kth.csc.moderndb.cbexplorer.parser.DefaultCitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.data.BikeData;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.io.File;
import java.util.Collection;

/**
 * Created by mhotan on 4/29/14.
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "se.kth.csc.moderndb.cbexplorer")
public class Application extends Neo4jConfiguration implements CommandLineRunner, CitiBikeReader {

    /**
     * The name of the database.
     */
    private static final String DATABASE_NAME = "citibike.db";

    @Autowired
    BikeRepository bikeRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    GraphDatabase graphDatabase;

    @Bean
    EmbeddedGraphDatabase graphDatabaseService() {

        return new EmbeddedGraphDatabase(DATABASE_NAME);
    }

    @Override
    public void run(String... args) throws Exception {
        DefaultCitiBikeParser parser = new DefaultCitiBikeParser(this);
        if (args.length == 0)
            parser.parse();
        else {
            String dirPath = args[0];
            File f = new File(dirPath);
            if (!f.exists())
                throw new IllegalArgumentException("File " + dirPath + " does not exist");
            parser.parse(f);
        }
    }

    public static void main(String[] args) throws Exception {
        FileUtils.deleteRecursively(new File(DATABASE_NAME));
        SpringApplication.run(Application.class, args);
    }


    public Station createStation(StationData data) {
        Station s = new Station(data.getStationId(), data.getName(), data.getLongitude(), data.getLatitude());
        System.out.println("Created Station " + s);
        return stationRepository.save(s);
    }


    public Trip createTrip(Bike bike, Station startStation, Station endStation, TripData data) {
        Trip t = new Trip(startStation, bike, data.getStartTime());
        t = tripRepository.save(t);
        System.out.println("Created Trip " + t.getId());

        // Pull the trip repository.
        t = tripRepository.findOne(t.getId());
        if (t == null) throw new RuntimeException("Unable to find saved trip " + t);
        t.setEndedAt(endStation);
        t.setEndTime(data.getEndTime().getTime());
        t.setUserType(data.getUserType());
        t.setUserBirthYear(data.getUserBirthYear());
        t.setUserGender(data.getUserGender());
        return tripRepository.save(t);
    }


    public Bike createBike(BikeData data) {
        Bike b = new Bike(data.getId());
        System.out.println("Created Bike " + b);
        return bikeRepository.save(b);
    }

    @Override
    public void addTrips(Collection<TripData> trips) {
        System.out.println("Adding " + trips.size() + " trips");

        for (TripData tripData : trips) {
            Bike bike = obtainBike(tripData.getBikeData());
            Station startStation = obtainStation(tripData.getStartStationData());
            Station endStation = obtainStation(tripData.getEndStationData());
            createTrip(bike, startStation, endStation, tripData);
        }
    }

    /**
     * Find or create a Bike instance corresponding to the data.
     *
     * @param bikeData The Bike data.
     * @return new or existing Bike with a matching id
     */
    private Bike obtainBike(BikeData bikeData) {
        Bike bike = bikeRepository.findByPropertyValue("bikeId", bikeData.getId());
        if (bike == null) {
            bike = createBike(bikeData);
        }
        return bike;
    }

    /**
     * Find or create a Station instance corresponding to the data.
     *
     * @param stationData The data of th station.
     * @return new or existing Station with a matching stationId
     */
    private Station obtainStation(StationData stationData) {
        Station station = stationRepository.findByPropertyValue("stationId", stationData.getStationId());
        if (station == null)
            station = createStation(stationData);
        return station;
    }
}
