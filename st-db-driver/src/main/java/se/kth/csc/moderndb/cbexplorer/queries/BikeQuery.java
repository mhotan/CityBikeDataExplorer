package se.kth.csc.moderndb.cbexplorer.queries;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import se.kth.csc.moderndb.cbexplorer.data.TripRoute;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeannine on 05.05.14.
 */
public class BikeQuery {

    protected JdbcTemplate jdbcTemplate;

    public BikeQuery() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource() {{
            setDriverClass(org.postgresql.Driver.class);
            setUsername(PostgreSQLDatabaseConnection.USERNAME);
            setUrl(PostgreSQLDatabaseConnection.URL + PostgreSQLDatabaseConnection.DATABASE_NAME);
            setPassword(PostgreSQLDatabaseConnection.PASSWORD);
        }};
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TripRoute> giveAllTripStationInformation() {
        System.out.println("Querying for trip station info");
        List<TripRoute> names = jdbcTemplate.query(
                "select * from " + PostgreSQLDatabaseConnection.TRIPROUTE,
                new RowMapper<TripRoute>() {
                    @Override
                    public TripRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return null;
                    }
                }
        );

        return names;
    }
}
