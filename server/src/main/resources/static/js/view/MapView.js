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

    // Keep track of all the markers
    var markers = [];

    // Keep track of all the marker click handlers
    var markerClickHandlers = [];

    // Calls on the model
    var createInfoWindow = function(station) {
        // Use the model to
        var contentString = '<h3>' + station.name + '</h3>';
        var infoView = new google.maps.InfoWindow({
            content: contentString,
            maxWidth: 200
        });
        return infoView;
    }

    // Show the station on the map.
    var showStation = function(station) {
        var stationPos = new google.maps.LatLng(station.latitude, station.longitude);
        var marker = new google.maps.Marker({
            position: stationPos,
            map : map
        });

        var infoWindow = createInfoWindow(station);
        var handler = google.maps.event.addListener(marker, 'click', function() {
            infoWindow.open(map, marker);
        });
        markerClickHandlers.push(handler);
    }

    // Initially populate the
    this.showStations = function(stations) {
        for (var i = 0; i < stations.length; i++) {
            showStation(stations[i]);
        }
    }

    var setAllMap = function(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }

    this.clearStations = function() {
        // Remove all the marker click handlers
        for (var i = 0; i < markerClickHandlers.length; i++) {
            google.maps.event.removeListener(markerClickHandlers[i]);
        }
        markerClickHandlers = []; // Clear the list of click handlers.

        setAllMap(null);
        markers = [];
    }

    // Register this view as an observer to the model.
    model.addObserver(this);

    // This method gets called when the model notifies the observers
    this.update = function(arg) {
        // TODO Update the Map view by the current state of the model.

    }
}
