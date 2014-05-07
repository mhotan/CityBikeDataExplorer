package se.kth.csc.moderndb.cbexplorer.queries;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import se.kth.csc.moderndb.cbexplorer.data.TripRoute;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeannine on 05.05.14.
 */
public class BikeQuery {

    protected static final String getXFromPoint = "ST_X(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";
    protected static final String getYFromPoint = "ST_Y(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";

    private static final SimpleDriverDataSource dataSource = new SimpleDriverDataSource() {{
        setDriverClass(org.postgresql.Driver.class);
        setUsername(PostgreSQLDatabaseConnection.USERNAME);
        setUrl(PostgreSQLDatabaseConnection.URL + PostgreSQLDatabaseConnection.DATABASE_NAME);
        setPassword(PostgreSQLDatabaseConnection.PASSWORD);
    }};
    protected static JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    public BikeQuery() {
        /*SimpleDriverDataSource dataSource = new SimpleDriverDataSource() {{
            setDriverClass(org.postgresql.Driver.class);
            setUsername(PostgreSQLDatabaseConnection.USERNAME);
            setUrl(PostgreSQLDatabaseConnection.URL + PostgreSQLDatabaseConnection.DATABASE_NAME);
            setPassword(PostgreSQLDatabaseConnection.PASSWORD);
        }};
        this.jdbcTemplate = new JdbcTemplate(dataSource);*/
    }

    public static List<TripRoute> giveAllTripStationInformation() {
        System.out.println("Querying for all trip station info");
        List<TripRoute> tripRoutes = jdbcTemplate.query(
                "select * from " + PostgreSQLDatabaseConnection.TRIPROUTE,
                new RowMapper<TripRoute>() {
                    @Override
                    public TripRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new TripRoute(rs.getLong(PostgreSQLDatabaseConnection.ID),
                                StationQuery.giveFullStationInformationAboutStationWithID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0),
                                StationQuery.giveFullStationInformationAboutStationWithID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0));
                    }
                }
        );

        for (TripRoute trip : tripRoutes) {
            System.out.println("id: " + trip.getId() + "... start: " + trip.getStart().getName() + " ... end: " + trip.getEnd().getName());
        }
        return tripRoutes;
    }

    public static TripRoute giveTripStationInformationForTripWithID(final long id) {
        System.out.println("Querying for trip station with given trip id");
        List<TripRoute> tripRoute = jdbcTemplate.query(
                "select " + PostgreSQLDatabaseConnection.STARTSTATION + ", " + PostgreSQLDatabaseConnection.ENDSTATION + " from " + PostgreSQLDatabaseConnection.TRIPROUTE + " where " + PostgreSQLDatabaseConnection.ID + " = ?",
                new Object[]{id},
                new RowMapper<TripRoute>() {
                    @Override
                    public TripRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new TripRoute(id,
                                StationQuery.giveFullStationInformationAboutStationWithID(rs.getLong(PostgreSQLDatabaseConnection.STARTSTATION)).get(0),
                                StationQuery.giveFullStationInformationAboutStationWithID(rs.getLong(PostgreSQLDatabaseConnection.ENDSTATION)).get(0));
                    }
                }
        );
        if (tripRoute.size() == 1) {
            System.out.println("id: " + tripRoute.get(0).getId() + "... start: " + tripRoute.get(0).getStart().getName() + " ... end: " + tripRoute.get(0).getEnd().getName());
            return tripRoute.get(0);
        } else return null;
    }


}

