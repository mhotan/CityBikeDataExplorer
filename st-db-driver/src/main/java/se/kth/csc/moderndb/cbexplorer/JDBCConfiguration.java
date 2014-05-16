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

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DRIVER_FULL_NAME = "org.postgresql.Driver";
    private static final String DATABASE_NAME = "citybike";
    private static final String USERNAME = "vagrant";
    private static final String PASSWORD = "vagrant";

    /**
     * @return JDBC Data source.
     */
    @Bean
    public DataSource PsqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_FULL_NAME);
        dataSource.setUrl(URL + DATABASE_NAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
