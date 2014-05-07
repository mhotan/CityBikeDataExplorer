package se.kth.csc.moderndb.cbexplorer.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import se.kth.csc.moderndb.cbexplorer.DatabaseConstants;
import se.kth.csc.moderndb.cbexplorer.core.repository.BikeRepository;
import se.kth.csc.moderndb.cbexplorer.core.services.BikeEventHandler;
import se.kth.csc.moderndb.cbexplorer.core.services.BikeService;

/**
 * Root Configuration of this application.
 *
 * Created by mhotan on 5/7/14.
 */
@Configuration
@EnableNeo4jRepositories
@ComponentScan(basePackages = "se.kth.csc.moderndb.cbexplorer.core.repository")
public class CoreConfig extends Neo4jConfiguration {

    public CoreConfig() {
        setBasePackage("se.kth.csc.moderndb.cbexplorer");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(DatabaseConstants.DATABASE_PATH);
    }

    @Autowired // Attempt to auto wire the bike repository.
    private BikeRepository bikeRepository;

    @Bean
    public BikeService createService(BikeRepository repo) {
        return new BikeEventHandler(repo);
    }

    @Bean
    public BikeRepository createBikeRepo() {
        return bikeRepository;
    }


}
