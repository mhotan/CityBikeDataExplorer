/**
 * Class that handles the drawing on the Google Map.
 * Argument - Requires a
 *
 * Created by mhotan on 5/11/14.
 */
var CitiBikeMapView = function(map, model) {

    // Add any additional view to already provided map.
    // Currently just hold a reference to the
    var map = map;

    // Create a reference to the model.
    var model = model;

    var markers = [];

    // Show the station on the map.
    var showStation = function(station) {
        var stationPos = new google.maps.LatLng(station.latitude, station.longitude);
        var marker = new google.maps.Marker({
            position: stationPos,
            map : map
        });
    }

    // Initially populate the
    this.showStations = function(stations) {
        for (var i = 0; i < stations.length; i++) {
            showStation(stations[i]);
        }
    }

    //
    this.update = function(arg) {
       // TODO Update the Map view by the current state of the model.

    }
}
