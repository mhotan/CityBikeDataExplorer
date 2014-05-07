package se.kth.csc.moderndb.cbexplorer.core.st_queries;

import se.kth.csc.moderndb.cbexplorer.core.domain.Station;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jeannine on 07.05.14.
 */
public class StationDAO {

    private DataSource dataSource;

    public StationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Station findByCustomerId(int custId){

        String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, custId);
            Station station = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                station = new Station(
                        rs.getLong(PostgreSQLDatabaseConnection.),
                        rs.getString("NAME"),
                        rs.getInt("Age")
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
                } catch (SQLException e) {}
            }
        }
    }
}
}
