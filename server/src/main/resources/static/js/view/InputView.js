/**
 * A wrapper around the view that controls user input.
 *
 * parameters
 *
 * Created by mhotan on 5/12/14.
 */
var InputView = function(container, model) {

    // Set the internal yet exposed members.
    this.root = container;
    this.model = model;


    // stationInfo can either be a name or an ID
    var searchStation = function(stationInfo){
        // TODO call query
    }

    var showStations = function(results){
        this.list = container.find("#stationResults");
        // TODO fill list
    }

    var searchBike = function(bikeID){
        // TODO call query
    }

    var showBikes = function(results){
        this.list = container.find("#bikeResults");
        // TODO fill list
    }

    var setTimeRangeForStations(minDate, maxDate){
    // Set date option
        $("#slider-range1").dateRangeSlider(
          "bounds":
              {
                 min: minDate,
                  max: maxDate
               });
    }

    var setTimeRangeForBikes(minDate, maxDate){
    // Set date option
        $("#slider-range2").dateRangeSlider(
          "bounds":
          {
            min: minDate,
            max: maxDate
        });
    }


    // Register this view as an observer to the model.
    model.addObserver(this);

    // This method gets called when the model notifies the observers
    this.update = function(arg) {
        // TODO Update the Input view by the current state of the model.

    }

}

