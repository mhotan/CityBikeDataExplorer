package se.kth.csc.moderndb.cbexplorer.core.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                });
        return results;
    }

    @Override
    public List<List<Station>> findStationPairsWithDistance(double distance) {
        return null;
    }

    @Override
    public Double findDistanceBtwStations(long station_id1, long station_id2) {
        return null;
    }

    //    @Override
//    public Double findDistanceBtwStations(long station_id1,long station_id2){
//        String sql = "SELECT ST_DISTANCE(JD.end_point,point)from "
//                + PostgreSQLDatabaseConnection.STATION +
//                ",(Select " + GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + "as end_point from "
//                +PostgreSQLDatabaseConnection.STATION+
//                "where ?= +" + PostgreSQLDatabaseConnection.STATIONID + ")JD where ?= "
//                + PostgreSQLDatabaseConnection.STATIONID+";";
//        Connection conn= null;
//        try {
//            conn = dataSource.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setLong(1, station_id1);
//            ps.setLong(2,station_id2);
//            ResultSet rs = ps.executeQuery();
//            rs.close();
//            ps.close();
//            return rs.getDouble(1);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                }
//            }
//        }
//
//    }

}
