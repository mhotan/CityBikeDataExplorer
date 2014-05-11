package se.kth.csc.moderndb.cbexplorer.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.kth.csc.moderndb.cbexplorer.Greeting;
import se.kth.csc.moderndb.cbexplorer.core.dao.StationDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.core.services.GraphService;
import se.kth.csc.moderndb.cbexplorer.rest.RestConstants;

import java.util.List;

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

    @Autowired
    StationDAOi stationDAO;

    @RequestMapping(method= RequestMethod.GET, value = "/hello")
    public @ResponseBody
    Greeting sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new Greeting(String.format(template, name));
    }

    // TODO Add more RESTful Spring method calls.

    @RequestMapping(method = RequestMethod.GET, value = RestConstants.BIKES_URI_PATH)
    public @ResponseBody
    List<Bike> findAllBikes() {
        return graphService.findAllBikes();
    }

    @RequestMapping(RestConstants.BIKES_URI_PATH + "/{bikeId}/tripCount")
    public @ResponseBody
    String getBikeTripCount(@PathVariable long bikeId) {
        return graphService.getBikeTripCount(bikeId).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = RestConstants.STATIONS_URI_PATH)
    public @ResponseBody
    List<Station> findAllStations() {return  graphService.findAllStations(); }

    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/byName/{name}")
    public @ResponseBody
    Station findStationByName(@PathVariable String name) {
        return graphService.findStationByName(name);
    }

    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/{stationId}/destinations")
    public @ResponseBody
    List<Station> getStationDestinations(@PathVariable long stationId) {
        return graphService.getStationDestinations(stationId);
    }

    @RequestMapping(RestConstants.STATIONS_URI_PATH + "/distance/{station1}/{station2}")
    public @ResponseBody
    String getDistanceBetweenStations(@PathVariable long station1, @PathVariable long station2) {
        return stationDAO.findDistanceBtwStations(station1, station2).toString();
    }

}
