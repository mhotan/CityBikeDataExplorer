package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.kth.csc.moderndb.cbexplorer.Greeting;
import se.kth.csc.moderndb.cbexplorer.parser.data.StationData;
import se.kth.csc.moderndb.cbexplorer.queries.StationQuery;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple controller for RESTful queries.
 *
 * Created by mhotan on 4/8/14.
 */
@Controller
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public @ResponseBody
    Greeting greeting(
            @RequestParam(value="name", required=false, defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


    @RequestMapping("/stations")
    public @ResponseBody
    List<StationData> stations() {
        StationQuery query = new StationQuery();
        return query.giveFullStationInformationAboutAllStations();
    }

    @RequestMapping("/station")
    public @ResponseBody
    List<StationData> station(
            @RequestParam(value="name", required=true) String name) {
        StationQuery query = new StationQuery();
        return query.giveFullStationInformationAboutStationNamed(name);
    }
}
