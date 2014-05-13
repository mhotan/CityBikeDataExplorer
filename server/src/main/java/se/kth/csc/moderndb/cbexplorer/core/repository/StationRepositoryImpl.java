package se.kth.csc.moderndb.cbexplorer.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import java.util.HashMap;
import java.util.Map;

import static se.kth.csc.moderndb.cbexplorer.DatabaseConstants.ENDS_AT_RELATION;
import static se.kth.csc.moderndb.cbexplorer.DatabaseConstants.STARTS_AT_RELATION;

/**
 * Created by corey on 5/9/14.
 */
public class StationRepositoryImpl implements StationRepositoryCustom {

    @Autowired
    private Neo4jTemplate template;

    @Override
    public Map<Long, Long> getDestinationCounts(Long graphId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("station", graphId);
        Result<Map<String, Object>> queryResult =
                template.query("START start=node({station}) MATCH start<-[:" + STARTS_AT_RELATION + "]-" +
                        "trip-[:" + ENDS_AT_RELATION + "]->end RETURN end.stationId, count(*)", params);

        Map<Long, Long> result = new HashMap<Long, Long>();
        for (Map<String, Object> row : queryResult) {
//            System.out.println(row);
            result.put((Long) row.get("end.stationId"), (Long) row.get("count(*)"));
        }
        return result;
    }

    @Override
    public Map<Long, Long> getArrivaleCounts(Long graphId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("station", graphId);
        Result<Map<String, Object>> queryResult =
                template.query("START end=node({station}) MATCH start<-[:" + STARTS_AT_RELATION + "]-" +
                        "trip-[:" + ENDS_AT_RELATION + "]->end RETURN start.stationId, count(*)", params);

        Map<Long, Long> result = new HashMap<Long, Long>();
        for (Map<String, Object> row : queryResult) {
//            System.out.println(row);
            result.put((Long) row.get("start.stationId"), (Long) row.get("count(*)"));
        }
        return result;
    }
}
