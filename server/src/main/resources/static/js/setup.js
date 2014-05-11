/**
 * Class that handles the initial set up of the web application.
 *
 * Created by mhotan on 5/10/14.
 */
var map;

// Keep track of all the current markers that are on the map.
var markers = [];

// The rectangle
var rectangle;

// Define the Type of station markers.
StationMarkerType = {
    SIMPLE : 0,
    USAGE_WEIGHTED : 1
}

function initialize() {
    // Initializat the Google Map view.
    map = new google.maps.Map(document.getElementById('map_canvas'), {
        zoom: 13,
        center: new google.maps.LatLng(40.7142,-74.0064)
    });

    // Set up the initial controller views.

    // Create a <script> tag and set the USGS URL as the source.
    var script = document.createElement('script');
    script.src = 'http://earthquake.usgs.gov/earthquakes/feed/geojsonp/2.5/week';
    document.getElementsByTagName('head')[0].appendChild(script);


}

function buildCurrentRequest() {

    // TODO Construct the current query.

}

// Starts to draw a rect
// https://developers.google.com/maps/documentation/javascript/examples/rectangle-event
function drawRectangle(topleft, bottomRight) {
    var bounds = new google.maps.LatLngBounds(topleft, bottomRight);

    // Define the rectangle and set its editable property to true.
    rectangle = new google.maps.Rectangle({
        bounds: bounds,
        editable: true,
        draggable: true
    });

    // The map enable the rectangle to draw itself on it.
    rectangle.setMap(map);

    // Add a listener
    google.maps.event.addListener(rectangle, 'bounds_changed', updateStations);
}

//
function updateStations(event) {

    // Update the current query for the current select
    // Find the current boundary provided by the rectangle
    // We don't have to use all of these.
    var bounds = rectangle.getBounds();
    var northEast = bounds.getNorthEast();
    var southWest = bounds.getSouthWest();
    var northEastLat = northEast.lat();
    var northEastLng = northEast.lng();
    var southWestLat = southWest.lat();
    var southWestLng = southWest.lng();

    // Package up values into a Spatial parameter type.


    // Build the current query.


    // Make the API call.

}

// Sets the map on all markers in the array.
function setAllMap(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Draw a marker at the station location specified
function showStation(station) {
    // Show a single station on the map.
    var lat = station.latitude;
    var long = station.longitude;

    // Currently drawing the simplest
    var myLatLong = new google.maps.LatLng(lat, long);
    var marker = new google.maps.Marker({
        position: myLatLong,
        map: map
    });
    markers.push(marker);
}

function showStations(stations) {
    // Add a marker for each station in the list of stations.
    for (var station in stations) {
        showStation(station);
    }
}

// Show the current trip on the screen.
function showTrip(trip) {
    // TODO Present a trip on the screen.

    // Create a marker and push it onto the markers queue.
}

// Show all the trips on the screen currently.
function showTrips(trips) {
    for (var trip in trips) {
        showTrip(trip);
    }
}


// Clears the map of all its content.
// Add a marker to the map and push to the array.
function clearMarkers() {
    // Clear all the markers from the screen.
    setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
    setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
    clearMarkers();
    markers = [];
}

// Loop through the results array and place a marker for each
// set of coordinates.
function eqfeed_callback(results){
    for (var i = 0; i < results.features.length; i++) {
        var earthquake = results.features[i];
        var coords = earthquake.geometry.coordinates;
        var latLng = new google.maps.LatLng(coords[1],coords[0]);
        var marker = new google.maps.Marker({
            position: latLng,
            map: map
        });
    }
}
google.maps.event.addDomListener(window, 'load', initialize);