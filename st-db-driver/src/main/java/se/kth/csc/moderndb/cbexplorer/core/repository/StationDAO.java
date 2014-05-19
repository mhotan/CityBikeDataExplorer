package se.kth.csc.moderndb.cbexplorer.core.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.PSQLConnection;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeannine on 07.05.14.
 */
public class StationDAO implements StationDAOi {

    // Cache Specifications
    private static final int CACHE_DEFAULT_EXPIRATION_TIME_MIN = 10; // In minutes
    private static final int CACHE_DEFAULT_SIZE = 10000; // Total number of trips

    // SQL Queries for extracting points.
    private static final String GET_X_FROM_POINT = "ST_X(" + PSQLConnection.POINT + "::geometry)";
    private static final String GET_Y_FROM_POINT = "ST_Y(" + PSQLConnection.POINT + "::geometry)";
    private static final String SELECT_FROM = "SELECT " + PSQLConnection.STATION_ID + ", " + PSQLConnection.NAME + ", " +
            GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PSQLConnection.STATION + " ";

    private JdbcTemplate jdbcTemplate;

    private final StationMapper stationMapper;

    @Autowired
    public StationDAO(DataSource source) {
        if (source == null)
            throw new NullPointerException("Can't have null datasource");
        this.jdbcTemplate = new JdbcTemplate(source);
        this.stationMapper = new StationMapper();
    }

    @Override
    public List<Station> findStationByID(long stationId) throws Exception {
        String sql = SELECT_FROM + "WHERE " + PSQLConnection.STATION_ID + " = ?";
        List<Station> results = jdbcTemplate.query(sql, new Object[]{stationId}, stationMapper);
        if (results.size() > 1) {
            throw new Exception("More than one station with same id");
        }
        return results;
    }

    @Override
    public List<Station> findAllStations() {
        return jdbcTemplate.query(SELECT_FROM.trim(), stationMapper);
    }

    @Override
    public List<Station> findStationByName(String name) {
        String sql = SELECT_FROM + "WHERE " + PSQLConnection.NAME + " = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, stationMapper);
    }

    @Override
    public List<List<Station>> findStationPairsWithDistance(double distance) {
        // TODO
        return null;
    }

    @Override
    public Double findDistanceBtwStations(long station_id1,long station_id2){
        String sql = "SELECT ST_DISTANCE(JD.end_point,point)from "
                + PSQLConnection.STATION +
                ",(Select " + GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + "as end_point from "
                +PSQLConnection.STATION+
                "where ?= +" + PSQLConnection.STATION_ID + ")JD where ?= "
                + PSQLConnection.STATION_ID +";";
        List<Double> distances = jdbcTemplate.query(sql, new Object[]{station_id1, station_id2},
                new RowMapper<Double>() {
                    @Override
                    public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getDouble(1);
                    }
                });
        if (distances.isEmpty())
            return null;
        else
            return distances.get(0);
    }

    // Station Row Mapper

    /**
     * Extracts Stations from JDBC queries.
     */
    private static class StationMapper implements RowMapper<Station> {

        private final Cache<Long, Station> stationCache;

        StationMapper() {
            this.stationCache = CacheBuilder.newBuilder()
                    .maximumSize(CACHE_DEFAULT_SIZE)
                    .expireAfterAccess(CACHE_DEFAULT_EXPIRATION_TIME_MIN, TimeUnit.MINUTES)
                    .build();
        }

        @Override
        public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Check if we have the station in the cache
            Long stationId = rs.getLong(PSQLConnection.STATION_ID);
            String name = rs.getString(PSQLConnection.NAME);
            Double longitude = rs.getDouble(3);
            Double latitude = rs.getDouble(4);

            Station station = stationCache.getIfPresent(stationId);
            if (station != null) {
                // Update the Cache value
                station.setName(name);
                station.setLongitude(longitude);
                station.setLatitude(latitude);
                return station;
            }

            // Cache currently does not include the station.
            // Add a new station to the cache and then return it.
            station = new Station(stationId, name, longitude, latitude);
            stationCache.put(stationId, station);
            return station;
        }
    }

}
