package se.kth.csc.moderndb.cbexplorer.core.st_queries;

import se.kth.csc.moderndb.cbexplorer.core.dataAccessObject.BikeDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
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
public class BikeDAO implements BikeDAOi {

    private DataSource dataSource;

    public BikeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Bike findBikeByID(long bikeID) {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.BIKEID +" FROM " + PostgreSQLDatabaseConnection.TRIP
                + " WHERE "+ PostgreSQLDatabaseConnection.BIKEID + " = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, bikeID);
            Bike bike = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
            }
            rs.close();
            ps.close();
            return bike;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    @Override
    public Bike findAllBikes() {
        String sql = "SELECT " + PostgreSQLDatabaseConnection.BIKEID +" FROM " + PostgreSQLDatabaseConnection.TRIP;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            Bike bike = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bike = new Bike(rs.getLong(PostgreSQLDatabaseConnection.BIKEID));
            }
            rs.close();
            ps.close();
            return bike;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
