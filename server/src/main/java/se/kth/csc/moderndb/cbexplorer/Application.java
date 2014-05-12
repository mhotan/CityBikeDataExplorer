package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import se.kth.csc.moderndb.cbexplorer.core.dao.*;
import se.kth.csc.moderndb.cbexplorer.core.repository.BikeRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.StationRepository;
import se.kth.csc.moderndb.cbexplorer.core.repository.TripRepository;
import se.kth.csc.moderndb.cbexplorer.core.services.GraphService;
import se.kth.csc.moderndb.cbexplorer.core.services.GraphServiceImpl;
import se.kth.csc.moderndb.cbexplorer.core.services.RelationalService;
import se.kth.csc.moderndb.cbexplorer.core.services.RelationalServiceImpl;
import se.kth.csc.moderndb.cbexplorer.domain.PSQLConnection;

import javax.sql.DataSource;

/**
 * Main class that the spring framework will find and run.  Spring will automatically
 * find this class.  {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration}
 * Is applied to this Application class that is placed in the root directory of the application.  This allows for the ability to
 * scan for classes within sub directories.
 *
 * Created by mhotan on 4/8/14.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration // Enable smart scanning for Spring Components
@EnableNeo4jRepositories(basePackages = "se.kth.csc.moderndb.cbexplorer.core.repository") // Set the base package to find Graph Repos
public class Application extends Neo4jConfiguration {

    private static final String MODEL_BASE_PACKAGE = "se.kth.csc.moderndb.cbexplorer.core.domain";
    private static final String GRAPH_BASE_PACKAGE = "se.kth.csc.moderndb.cbexplorer.graph.core.domain";


    @Autowired
    private Neo4jTemplate graphTemplate;

    public Application() {
        setBasePackage(MODEL_BASE_PACKAGE, GRAPH_BASE_PACKAGE);
        System.out.println("Base Package is set to " + MODEL_BASE_PACKAGE + ", " + GRAPH_BASE_PACKAGE);
    }

    /**
     * Start the application
     * @param args Ignored Program arguments.
     */
    public static void main(String[] args) {
        //  Create an application context.
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    /**
     * @return The Graph Database Service for different Graph Repositories
     */
    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(DatabaseConstants.DATABASE_PATH);
    }

    /**
     * Provides an instance of the Graph Service.  The Graph Service is in charge of
     * logic functionality.
     *
     * @param bikeRepository Bike Repo
     * @param tripRepository Trip Repo
     * @param stationRepository Station Repo.
     * @return Graph Service.
     */
    @Bean
    @Autowired
    public GraphService getGraphService(BikeRepository bikeRepository,
                                        TripRepository tripRepository,
                                        StationRepository stationRepository) {
        return new GraphServiceImpl(bikeRepository, tripRepository, stationRepository);
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(PSQLConnection.DRIVER_FULL_NAME);
        dataSource.setUrl(PSQLConnection.URL + PSQLConnection.DATABASE_NAME);
        dataSource.setUsername(PSQLConnection.USERNAME);
        dataSource.setPassword(PSQLConnection.PASSWORD);
        return dataSource;
    }

    @Bean
    @Autowired
    public BikeDAOi getBikeDAO(DataSource dataSource) {
        return new BikeDAO(dataSource);
    }

    @Bean
    @Autowired
    public StationDAOi getStationDAO(DataSource dataSource) {
        return new StationDAO(dataSource);
    }

    @Bean
    @Autowired
    public TripDAOi getTripDAO(DataSource dataSource, StationDAOi stationDAO) {
        return new TripDAO(dataSource, stationDAO);
    }

    @Bean
    @Autowired
    public RelationalService getRelationService(TripDAOi tripDAO, StationDAOi stationDAO, BikeDAOi bikeDAO) {
        return new RelationalServiceImpl(tripDAO, stationDAO, bikeDAO);
    }

    // TODO Declare more Spring Beans here.

}
