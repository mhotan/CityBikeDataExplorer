package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.PSQLConnection;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeannine on 07.05.14.
 */
public class BikeDAO implements BikeDAOi {

    private static final int BIKE_ID_COLUMN = 1;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BikeDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Bike> findAllBikes() {
        String sql = "SELECT DISTINCT " + PSQLConnection.BIKE_ID + " FROM " + PSQLConnection.TRIP;
        return jdbcTemplate.query(sql, new Object[]{}, BikeMapper.getInstance());
    }

    /**
     * Mapping class that maps a single bike id to a bike class.
     */
    private static class BikeMapper implements RowMapper<Bike> {

        private static BikeMapper instance;

        static BikeMapper getInstance() {
            if (instance == null)
                instance = new BikeMapper();
            return instance;
        }

        private BikeMapper() {}

        @Override
        public Bike mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Bike(rs.getLong(BIKE_ID_COLUMN));
        }
    }
}
