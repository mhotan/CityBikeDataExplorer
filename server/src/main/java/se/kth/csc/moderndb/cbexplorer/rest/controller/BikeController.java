package se.kth.csc.moderndb.cbexplorer.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.kth.csc.moderndb.cbexplorer.core.domain.Bike;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.AllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.BikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestAllBikesEvent;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestBikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.services.BikeService;
import se.kth.csc.moderndb.cbexplorer.rest.RestContants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by mhotan on 5/7/14.
 */
@Controller
@RequestMapping(RestContants.AGGREGATORS_URI_PATH + RestContants.BIKES_URI_PATH)
public class BikeController {

    private static Logger LOG = LoggerFactory.getLogger(BikeController.class);

    @Autowired
    private BikeService bikeService;

    /**
     * Returns all the known bikes from this data set.
     *
     * @return List of all the available bikes.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Bike> getAllBikes() {
        AllBikesEvent event = bikeService.requestAllBikes(new RequestAllBikesEvent());
        final List<Bike> list = new ArrayList<Bike>();
        event.getBikes().stream().forEach(new Consumer<Bike>() {
            @Override
            public void accept(Bike bike) {
                list.add(bike);
            }
        });
        return list;
    }

    /**
     * Returns the order with the specific ID.
     *
     * @param id Citibike ID of the bike
     * @return The bike with the ID if it exists.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Bike> getOrder(@PathVariable long id) {
        BikeEvent event = bikeService.requestBike(new RequestBikeEvent(id));
        if (!event.isEntityFound()) {
            return new ResponseEntity<Bike>(HttpStatus.NOT_FOUND);
        }
        Bike bike = event.getBike();
        return new ResponseEntity<Bike>(bike, HttpStatus.OK);
    }

}
