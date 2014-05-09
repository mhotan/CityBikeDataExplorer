package se.kth.csc.moderndb.cbexplorer.core.dao;

import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jeannine on 07.05.14.
 */
public class StationDAO implements StationDAOi {

    private static final String GET_X_FROM_POINT = "ST_X(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";
    private static final String GET_Y_FROM_POINT = "ST_Y(" + PostgreSQLDatabaseConnection.POINT + "::geometry)";
    private DataSource dataSource;


    public StationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Station findStationByID(long stationId) {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION +
                " WHERE " + PostgreSQLDatabaseConnection.STATIONID + " = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, stationId);
            Station station = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                station = new Station(
                        rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                        rs.getString(PostgreSQLDatabaseConnection.NAME),
                        rs.getDouble(3),
                        rs.getDouble(4)
                );
            }
            rs.close();
            ps.close();
            return station;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    @Override
    public Station findAllStations() {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            Station station = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                station = new Station(
                        rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                        rs.getString(PostgreSQLDatabaseConnection.NAME),
                        rs.getDouble(3),
                        rs.getDouble(4)
                );
            }
            rs.close();
            ps.close();
            return station;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public Station findStationByName(String name) {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.STATIONID + ", " + PostgreSQLDatabaseConnection.NAME + ", " +
                GET_X_FROM_POINT + "," + GET_Y_FROM_POINT + " FROM " + PostgreSQLDatabaseConnection.STATION +
                " WHERE " + PostgreSQLDatabaseConnection.NAME + " = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            Station station = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                station = new Station(
                        rs.getLong(PostgreSQLDatabaseConnection.STATIONID),
                        rs.getString(PostgreSQLDatabaseConnection.NAME),
                        rs.getDouble(3),
                        rs.getDouble(4)
                );
            }
            rs.close();
            ps.close();
            return station;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
