package se.kth.csc.moderndb.cbexplorer.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;
import se.kth.csc.moderndb.cbexplorer.core.params.DefaultParameterSettings;
import se.kth.csc.moderndb.cbexplorer.core.params.TemporalParameters;
import se.kth.csc.moderndb.cbexplorer.core.range.ShortRange;
import se.kth.csc.moderndb.cbexplorer.core.range.TimeRange;
import se.kth.csc.moderndb.cbexplorer.core.services.GraphService;
import se.kth.csc.moderndb.cbexplorer.core.services.RelationalService;
import se.kth.csc.moderndb.cbexplorer.rest.RestConstants;

import java.util.List;
import java.util.Map;

/**
 * Citibike REST Endpoint class that is implemented using Spring MVC Framework.  Controller manages
 * request for analytical related data.  Response are sent in response bodies as JSON objects.  Jackson 2 library
 * allows for the automatic marshalling and unmarshalling of JSON objects.
 *
 * Created by mhotan on 5/9/14.
 */
@Controller
@RequestMapping(RestConstants.API_URI_PATH)
public class CitiBikeController {

    private static final String template = "Hello, %s!";

    @Autowired
    GraphService graphService;

    // TODO The DAOs should not be directly expose at this layer.
    // It reveals to much about the underlying structure of the PSQL database.
    // Refactor the DAO into a service that connects to relation database.
    // Potential name RelationService

    @Autowired
    RelationalService relationalService;

    /**
     * @return The default parameter settings for all the possible queries.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/parameters/default")
    public @ResponseBody
    DefaultParameterSettings getDefaultParameters() {
        // Extract the default Bike Parameters
//        List<Bike> bikes = relationalService.getAllBikes();
//        List<Station> stations = relationalService.getAllStations();
        TemporalParameters temporalParameters = new TemporalParameters(
                relationalService.getTimeRange(),
                relationalService.getDurationLimits());
        ShortRange birthRange = relationalService.getBirthYearLimits();
        return new DefaultParameterSettings(temporalParameters, birthRange);
    }

    @RequestMapping(method = RequestMethod.GET, value = RestConstants.BIKES_URI_PATH)
    public @ResponseBody
    List<Bike> findAllBikes() {
        return relationalService.getAllBikes();
    }

    @RequestMapping(RestConstants.BIKES_URI_PATH + "/{bikeId}/tripCount")
    public @ResponseBody
    String getBikeTripCount(@PathVariable long bikeId) {
        return graphService.getBikeTripCount(bikeId).toString();
    }

    @RequestMapping(RestConstants.BIKES_URI_PATH + "/{bikeId}/trips/{startTime}/{endTime}")
    public @ResponseBody
    List<Trip> getBikeTrips(@PathVariable long bikeId, @PathVariable long startTime, @PathVariable long endTime) {
        return graphService.getBikeTrips(bikeId, startTime, endTime);
    }

    @RequestMapping(method = RequestMethod.GET, value = RestConstants.STATIONS_URI_PATH)
    public @ResponseBody
    List<Station> findAllStations() {return  relationalService.getAllStations(); }

    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/byName/{name}")
    public @ResponseBody
    Station findStationByName(@PathVariable String name) {
        return graphService.findStationByName(name);
    }

//    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/pairsWithDistance/{distance}")
//    public @ResponseBody
//    List<List<Station>> findStationPairsWithDistance(@PathVariable double distance) {
//        return stationDAO.findStationPairsWithDistance(distance);
//    }

    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/{stationId}/destinations")
    public @ResponseBody
    Map<Long, Long> getStationDestinations(@PathVariable long stationId) {
        return graphService.getStationDestinations(stationId);
    }

    @RequestMapping(RestConstants.TRIPS_URI_PATH + "/timeRange")
    public @ResponseBody
    TimeRange getTimeRange() {
        return relationalService.getTimeRange();
    }

//    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/statistics")
//    public @ResponseBody
//    Map<Long, StationUsageStatistics> getAllStationStatistics() {
//        return graphService.findAllStationStatistics();
//    }

//    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/{stationId}/statistics")
//    public @ResponseBody
//    StationUsageStatistics getStationStatistic(@PathVariable long stationId) {
//        return graphService.findStationStatistics(stationId);
//    }


}
