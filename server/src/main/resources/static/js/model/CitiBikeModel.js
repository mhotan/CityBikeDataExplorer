/**
 * Potentially can do some automatic
 *
 * Created by mhotan on 5/11/14.
 */
var CitiBikeModel = function() {

    // Cache all the stations locally
    var stations = {};

    // Cache all the bikes locally
    var bikes = {};

    // Current group of selected stations
    var selectedStations = [];

    // Current group of selected bikes.
    var selectedBikes = [];

    // Single selected station
    var singleSelectedStation = null;
    // Map of all the trips
    var currentDestinations = null;

    var model = this;

    // The complete time range between two
    var timeRange = null;

    // Default Parameters
    var defaultParams = null;

    // Returns all the current bikes
    this.getAllBikes = function(callback) {

        // Check the runtime memory
        // Minimize unecesary REST calls
        if ($.isEmptyObject(bikes)) {
            CitiBikeApi.findAllBikes(function (result) {
                for (var i = 0; i < result.length; i++) {
                    // Map the Bike Id to the bike itself
                    bikes[result[i]['bikeId']] = result[i];
                }
                // Populate the select stations.

                callback(bikes);
            });
        } else {
            callback(bikes)
        }
    };

    // Returns all the selected bikes potentially
    this.getSelectedBikes = function(callback) {
        if ($.isEmptyObject(bikes)) {
            model.getAllBikes(function(result) {
                // Ignore the result because getAllBikes would populate the
                callback(selectedBikes);
            });
        } else {
            callback(selectedBikes);
        }
    };

    // Returns the all the current stations
    this.getAllStations = function(callback) {

        // Check the runtime memory
        // Minimize unecesary REST calls
        if ($.isEmptyObject(stations)) {
            CitiBikeApi.findAllStations(function (result) {
                for (var i = 0; i < result.length; i++) {
                    stations[result[i]['stationId']] = result[i];
                }
                callback(stations);
            });
        } else {
            callback(stations);
        }
    };

    // Return the selected stations.
    this.getSelectedStations = function(callback) {

        // If there are no stations then we need to request it from the server
        if ($.isEmptyObject(stations)) {
            model.getAllStations(function (result) {
                callback(selectedStations);
            });
        } else {
            // Otherwise send back the current list of selected stations.
            callback(selectedStations);
        }
    };

    this.getTimeRange = function(callback) {
        if (timeRange != null) return callback(timeRange);
        CitiBikeApi.getTimeRange(function(result) {
            timeRange = result;
            timeRange.minDate = new Date(timeRange.min);
            timeRange.maxDate = new Date(timeRange.max);
            callback(timeRange);
        });
    };

    // Change the state of the current selected stations

    // Add a bike to the selected group.
    this.addSelectedBike = function(bike) {
        for (var i = 0; i < selectedBikes.length; i++) {
            if (selectedBikes[i].bikeId == bike.bikeId) return;
        }
        selectedBikes.push(bike);
        notifyObservers();
    };

    // Remove bike from selected
    this.removeSelectedBike = function(bike) {
        var toRemove = -1;
        for (var i = 0; i < selectedBikes.length; i++) {
            if (selectedBikes[i].bikeId == bike.bikeId) {
                toRemove = i;
                break;
            }
        }
        if (toRemove > -1) {
            selectedBikes.splice(toRemove, 1);
            notifyObservers();
        }
    };

    this.clearSelectedBikes = function() {
        if (selectedBikes.length == 0) return;
        selectedBikes = [];
        notifyObservers();
    };

    // Adds a station to the selected.
    this.addSelectedStation = function(station) {
        for (var i = 0; i < selectedStations.length; i++) {
            if (selectedStations[i].stationId == station.stationId) return;
        }
        selectedStations.push(station);
        notifyObservers();
    };

    this.removeSelectedStation = function(station) {
        var toRemove = -1;
        for (var i = 0; i < selectedStations.length; i++) {
            if (selectedStations[i].stationId == station.stationId) {
                toRemove = i;
                break;
            }
        }
        if (toRemove > -1) {
            selectedStations.splice(toRemove, 1);
            notifyObservers();
        }
    };

    this.clearSelectedStations = function() {
        if (selectedStations.length == 0) return;
        selectedStations = [];
        notifyObservers();
    };

    this.getDefaultParameters = function(callback) {
        if (defaultParams != null)
            callback(defaultParams);
        CitiBikeApi.getDefaultParameters(function(result) {
            defaultParams = result;
            callback(result);
        });
    };

    this.getSingleSelectedStation = function() {
        return singleSelectedStation;
    };

    this.getCurrentDestinations = function() {
        return currentDestinations;
    };

    this.setSelectedStation = function(station) {
        // refresh the screen.
        singleSelectedStation = station;
        currentDestinations = null;
        notifyObservers();

        // Make the API call.
        CitiBikeApi.getStationDestinations(station['stationId'], function(result) {
            currentDestinations = result;
            notifyObservers();
        });
    };

    /*****************************************
     Observable implementation
     *****************************************/

    // Internal list of observers
    var observers = [];

    // Add an observer to this
    this.addObserver = function(observer) {
        observers.push(observer);
    }

    // Notify any object that there was a change in state.
    var notifyObservers = function(arg) {
        for (var i = 0; i < observers.length; i++) {
            observers[i].update(arg);
        }
    }

}