package se.kth.csc.moderndb.cbexplorer.queries;

import org.postgis.Point;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.PSQLConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeannine on 04.05.14.
 */
public class StationQuery extends BikeQuery{


    //private JdbcTemplate jdbcTemplate;


    public static List<String> giveAllStationNames() {
        System.out.println("Querying for stations names");
        List<String> names = jdbcTemplate.query(
                "select " + PSQLConnection.NAME + " from " + PSQLConnection.STATION,
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(PSQLConnection.NAME);
                    }
                }
        );
        for (String name : names) {
            System.out.println(name);
        }
        return names;
    }

    public static List<StationData> giveFullStationInformationAboutAllStations() {
        System.out.println("Querying for stations");
        List<StationData> results = jdbcTemplate.query(
                "select * from " + PSQLConnection.STATION,
                new RowMapper<StationData>() {
                    @Override
                    public StationData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Point p = givePointForStationWithID(rs.getLong(PSQLConnection.STATION_ID));
                        return new StationData(rs.getLong(PSQLConnection.STATION_ID), rs.getString(PSQLConnection.NAME),
                                p.getX(), p.getY());
                    }
                }
        );


        for (StationData station : results) {
            System.out.println(station.getName() + "... longitude: " + station.getLongitude() + "... latitude:" + station.getLatitude());
        }
        return results;
    }

    public static List<StationData> giveFullStationInformationAboutStationNamed(final String name) {
        System.out.println("Querying for station named" + name);
        List<StationData> result = jdbcTemplate.query("select " + PSQLConnection.STATION_ID + ", " + getXFromPoint + ", " + getYFromPoint + "from " + PSQLConnection.STATION + " where " + PSQLConnection.NAME + " = ?",
                new Object[]{name},
                new RowMapper<StationData>() {
                    @Override
                    public StationData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new StationData(rs.getLong(PSQLConnection.STATION_ID), name, rs.getDouble(2), rs.getDouble(3));
                    }
                }
        );
        for (StationData station : result) {
            System.out.println(station.getName() + "... longitude: " + station.getLongitude() + "... latitude:" + station.getLatitude());
        }
        return result;
    }

    public static List<StationData> giveFullStationInformationAboutStationWithID(final Long id) {
        System.out.println("Querying for station with id" + id);
        List<StationData> result = jdbcTemplate.query("select " + PSQLConnection.NAME + ", " + getXFromPoint + ", " + getYFromPoint + "from " + PSQLConnection.STATION + " where " + PSQLConnection.STATION_ID + " = ?",
                new Object[]{id},
                new RowMapper<StationData>() {
                    @Override
                    public StationData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new StationData(id, rs.getString(PSQLConnection.NAME), rs.getDouble(2), rs.getDouble(3));
                    }
                }
        );
        return result;
    }

    public static Point givePointForStationWithID(long id) {
        System.out.println("Querying for point with id");
        List<Point> result = jdbcTemplate.query(
                "select " + getXFromPoint + ", " + getYFromPoint + " from " + PSQLConnection.STATION + " where " + PSQLConnection.STATION_ID + " = ?", new Object[]{id},
                new RowMapper<Point>() {
                    @Override
                    public Point mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Point(rs.getDouble(1), rs.getDouble(2));
                    }
                }
        );

        if (result.size() == 1) {
            return result.get(0);
        } else
            return null;
    }

    public List<Point> givePointForStationNamed(String name) {
        System.out.println("Querying for point with name");
        List<Point> result = jdbcTemplate.query(
                "select " + getXFromPoint + ", " + getYFromPoint + " from " + PSQLConnection.STATION + " where " + PSQLConnection.NAME + " = ?", new Object[]{name},
                new RowMapper<Point>() {
                    @Override
                    public Point mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Point(rs.getDouble(1), rs.getDouble(2));
                    }
                }
        );

        return result;
    }


}
