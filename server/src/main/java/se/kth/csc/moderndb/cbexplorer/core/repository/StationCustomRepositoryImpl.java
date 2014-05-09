package se.kth.csc.moderndb.cbexplorer.core.repository;

/**
 * Created by corey on 5/9/14.
 */
public class StationCustomRepositoryImpl implements StationCustomRepository {
    /*
    @Autowired

    private Neo4jTemplate template;

    @Override
    public Map<Station, Long> getDestinationCounts(Long graphId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("station", graphId);
        Result<Map<String, Object>> queryResult =
                template.query("START start=node({station}) MATCH start<-[:" + STARTS_AT_RELATION + "]-" +
                "trip-[:" + ENDS_AT_RELATION + "]->end RETURN end, count(*)", params);

        Map<Station, Long> result = new HashMap<Station, Long>();
        for (Map<String, Object> row : queryResult) {
            result.put((Station) row.get("end"), (Long) row.get("count(*)"));
        }
        return result;
    }
    */
}
