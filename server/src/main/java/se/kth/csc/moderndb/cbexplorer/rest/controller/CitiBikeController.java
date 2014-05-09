package se.kth.csc.moderndb.cbexplorer.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.kth.csc.moderndb.cbexplorer.Greeting;
import se.kth.csc.moderndb.cbexplorer.core.dao.StationDAOi;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.domain.Station;
import se.kth.csc.moderndb.cbexplorer.core.services.GraphService;
import se.kth.csc.moderndb.cbexplorer.rest.RestContants;

import java.util.List;

/**
 * Citibike REST Endpoint class that is implemented using Spring MVC Framework.  Controller manages
 * request for analytical related data.  Response are sent in response bodies as JSON objects.  Jackson 2 library
 * allows for the automatic marshalling and unmarshalling of JSON objects.
 *
 * Created by mhotan on 5/9/14.
 */
@Controller
@RequestMapping(RestContants.API_URI_PATH)
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

    @RequestMapping(method = RequestMethod.GET, value = RestContants.BIKES_URI_PATH)
    public @ResponseBody
    List<Bike> getAllBikes() {
        return graphService.requestAllBikes();
    }

    @RequestMapping(method = RequestMethod.GET, value = RestContants.STATIONS_URI_PATH)
    public @ResponseBody
    List<Station> getAllStations() {return  graphService.requestAllStations(); }

}
