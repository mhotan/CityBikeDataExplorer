package se.kth.csc.moderndb.cbexplorer.queries;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.kth.csc.moderndb.cbexplorer.domain.PostgreSQLDatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeannine on 05.05.14.
 */
public class TripQuery extends BikeQuery{

    private JdbcTemplate jdbcTemplate;

    public TripQuery() {
        super();
        this.jdbcTemplate = super.jdbcTemplate;
    }



}
