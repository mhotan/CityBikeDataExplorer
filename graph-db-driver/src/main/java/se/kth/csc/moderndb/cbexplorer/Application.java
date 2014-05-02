package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by mhotan on 4/28/14.
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "se.kth.csc.moderndb.cbexplorer")
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
public class Application extends Neo4jConfiguration {

    public Application() {
        setBasePackage("se.kth.csc.moderndb.cbexplorer");
    }

    /**
     * The name of the database.
     */
    protected static final String DATABASE_NAME = "cbexplorer.db";

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(DATABASE_NAME);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
