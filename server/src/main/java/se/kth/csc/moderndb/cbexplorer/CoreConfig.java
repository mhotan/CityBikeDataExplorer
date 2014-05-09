package se.kth.csc.moderndb.cbexplorer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import se.kth.csc.moderndb.cbexplorer.core.services.BikeService;

/**
 * Created by mhotan on 5/7/14.
 */
@Configuration
public class CoreConfig extends Neo4jConfiguration {

    @Bean
    public BikeService createBikeService() {
        // TODO return an instance of a Bike Service.
        return null;
    }

}
