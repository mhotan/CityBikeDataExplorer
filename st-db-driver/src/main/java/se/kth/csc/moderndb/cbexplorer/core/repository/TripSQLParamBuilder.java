package se.kth.csc.moderndb.cbexplorer.core.repository;

import se.kth.csc.moderndb.cbexplorer.PSQLConnection;
import se.kth.csc.moderndb.cbexplorer.core.params.BikeParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.TemporalParameters;
import se.kth.csc.moderndb.cbexplorer.core.params.UserParameters;

/**
 * Created by mhotan on 5/18/14.
 */
public class TripSQLParamBuilder {

    private static final String START_STATION_WHERE_CLAUSE = PSQLConnection.START_STATION + " = ?";
    private static final String END_STATION_WHERE_CLAUSE = PSQLConnection.START_STATION + " = ?";

    private Long startStation, endStation;

    private UserParameters userParameters;

    private BikeParameters bikeParameters;

    private TemporalParameters temporalParameters;

    public void setStartStation(Long startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(Long endStation) {
        this.endStation = endStation;
    }

    public void setUserParameters(UserParameters userParameters) {
        this.userParameters = userParameters;
    }

    public void setBikeParameters(BikeParameters bikeParameters) {
        this.bikeParameters = bikeParameters;
    }

    public void setTemporalParameters(TemporalParameters temporalParameters) {
        this.temporalParameters = temporalParameters;
    }

    static class Clause {

        private final String clause;
        private final Object argument;

        private Clause(String clause, Object argument) {
            this.clause = clause;
            this.argument = argument;
        }

        public String getClause() {
            return clause;
        }

        public Object getArgument() {
            return argument;
        }
    }

}
