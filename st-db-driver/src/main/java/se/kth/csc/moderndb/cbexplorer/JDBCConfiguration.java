package se.kth.csc.moderndb.cbexplorer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by mhotan on 5/16/14.
 */
@Configuration
public class JDBCConfiguration {

    // Postgresql values
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DRIVER_FULL_NAME = "org.postgresql.Driver";
    private static final String DATABASE_NAME = "citibike";
    private static final String USERNAME = "vagrant";
    private static final String PASSWORD = "vagrant";

    // Postgis
    private static final String URL_POSTGIS = "jdbc:postgresql_postGIS://localhost:5432/";
    private static final String DRIVER_FULL_NAME_POSTGIS = "org.postgis.DriverWrapper";

    /**
     * @return Data source to connect with SQL database.
     */
    @Bean
    public DataSource PSQLDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_FULL_NAME_POSTGIS);
        dataSource.setUrl(URL_POSTGIS + DATABASE_NAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
