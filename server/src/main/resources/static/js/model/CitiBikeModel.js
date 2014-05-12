/**
 * Potentially can do some automatic
 *
 * Created by mhotan on 5/11/14.
 */
var CitiBikeModel = function() {

    // Cache all the stations locally
    var stations = [];

    // Cache all the bikes locally
    var bikes = [];

    // Current group of selected stations
    var selectedStations = [];

    // Current group of selected bikes.
    var selectedBikes = [];

    var model = this;

    // The complete time range between two
    var timeRange = {};

    // Returns all the current bikes
    this.getAllBikes = function(callback) {

        // Check the runtime memory
        // Minimize unecesary REST calls
        if (bikes.length == 0) {
            CitiBikeApi.findAllBikes(function (result) {
                bikes = result;
                selectedBikes = result.slice(0); // Copy by value
                // After the call is complete notify t
                callback(bikes);
            });
        } else {
            callback(bikes)
        }
    }

    // Returns the all the current stations
    this.getAllStations = function(callback) {

        // Check the runtime memory
        // Minimize unecesary REST calls
        if (stations.length == 0) {
            CitiBikeApi.findAllStations(function (result) {
                stations = result;
                selectedStations = stations.slice(0); // Copy by value
                callback(stations);
            });
        } else {
            callback(stations);
        }
    }

    // Return the selected stations.
    this.getSelectedStations = function(callback) {

        // If there are no stations then we need to request it from the server
        if (stations.length == 0) {
            model.getAllStations(function (result) {
                callback(selectedStations);
            });
        } else {
            // Otherwise send back the current list of selected stations.
            callback(selectedStations);
        }
    }

    // Adds a station to the selected.
    this.addSelectedStation = function(station) {
        for (var i = 0; i < selectedStations.length; i++) {
            if (selectedStations[i].stationId == station.stationId) return;
        }
        selectedStations.push(station);
        notifyObservers();
    }

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
        }
        notifyObservers();
    }

    this.clearSelectedStations = function() {
        selectedStations = [];
        notifyObservers();
    }



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