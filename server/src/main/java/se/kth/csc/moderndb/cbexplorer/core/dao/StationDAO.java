package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeannine on 07.05.14.
 */
public class StationDAO implements StationDAOi {

    private static final String GET_X_FROM_POINT = "ST_X(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";
    private static final String GET_Y_FROM_POINT = "ST_Y(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";

    private JdbcTemplate jdbcTemplate;


    public StationDAO(DataSource source) {
//        SimpleDriverDataSource driverDataSource = new SimpleDriverDataSource() {{
//            setDriverClass(org.postgresql.Driver.class);
//            setUsername(PostgreSQLDatabaseConnection.USERNAME);
//            setUrl(PostgreSQLDatabaseConnection.URL + PostgreSQLDatabaseConnection.DATABASE_NAME);
//            setPassword(PostgreSQLDatabaseConnection.PASSWORD);
//        }};
        this.jdbcTemplate = new JdbcTemplate(source);
    }

    @Override
    public List<Station> findStationByID(long stationId) throws Exception {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION +
                " WHERE " + PostgreSQLDatabaseConnection.STATIONID + " = ?";
        List<Station> results = jdbcTemplate.query(
                sql, new Object[]{stationId},
                new RowMapper<Station>() {
                    @Override
                    public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Station(
                                rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                                rs.getString(PostgreSQLDatabaseConnection.NAME),
                                rs.getDouble(3),
                                rs.getDouble(4)
                        );
                    }
                }
        );
        if (results.size() > 1) {
            throw new Exception("More than one station with same id");
        }
        return results;
    }

    @Override
    public List<Station> findAllStations() {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION;
        List<Station> results = jdbcTemplate.query(
                sql,
                new RowMapper<Station>() {
                    @Override
                    public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Station(
                                rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                                rs.getString(PostgreSQLDatabaseConnection.NAME),
                                rs.getDouble(3),
                                rs.getDouble(4)
                        );
                    }
                }
        );
        return results;
    }

    @Override
    public List<Station> findStationByName(String name) {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION +
                " WHERE " + PostgreSQLDatabaseConnection.NAME + " = ?";
        List<Station> results = jdbcTemplate.query(
                sql, new Object[]{name},
                new RowMapper<Station>() {
                    @Override
                    public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Station(
                                rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                                rs.getString(PostgreSQLDatabaseConnection.NAME),
                                rs.getDouble(3),
                                rs.getDouble(4)
                        );
                    }
                }
        );
        return results;
    }

    @Override
    public List<List<Station>> findStationPairsWithDistance(double distance) {
        String sql = "SELECT DISTINCT a." + PostgreSQLDatabaseConnection.STATIONID + " as start,JD." + PostgreSQLDatabaseConnection.STATIONID +
                " as end FROM " + PostgreSQLDatabaseConnection.STATION + " a, (SELECT " + PostgreSQLDatabaseConnection.POINT + ", " + PostgreSQLDatabaseConnection.STATIONID +
                " FROM " + PostgreSQLDatabaseConnection.STATION + ")JD WHERE ST_DWITHIN(a." + PostgreSQLDatabaseConnection.POINT + " ,JD." + PostgreSQLDatabaseConnection.POINT + ", ?) = true AND JD." +
                PostgreSQLDatabaseConnection.STATIONID + "< a." + PostgreSQLDatabaseConnection.STATIONID;
        List<List<Station>> results = jdbcTemplate.query(
                sql, new Object[]{distance},
                new RowMapper<List<Station>>() {
                    @Override
                    public List<Station> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        List<Station> pair = new ArrayList<Station>();
                        try {
                            List<Station> start = findStationByID(rs.getLong(1));
                            List<Station> end = findStationByID(rs.getLong(2));
                            pair.addAll(start);
                            pair.addAll(end);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return pair;
                    }
                }
        );
        return results;
    }

}
