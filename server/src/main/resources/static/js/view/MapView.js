/**
 * Class that handles the drawing on the Google Map.
 * Argument - Requires a
 *
 * Created by mhotan on 5/11/14.
 */
var CitiBikeMapView = function(map, model) {

    // Add any additional view to already provided map.
    // Currently just hold a reference to the
    this.map = map;

    // Create a reference to the model.
    this.model = model;

    // Keep track of all the markers
    var markers = [];

    // Keep track of all the marker click handlers
    var markerClickHandlers = [];

    // Show all
    var trips = [];

    // The Path symbol for drawing a trip.
    var lineSymbol = {
        path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
        scale: 5,
        strokeColor: '#393'
    };

    var drawLine = function(lineCoordinates) {
        var line = new google.maps.Polyline({
            path: lineCoordinates,
            icons: [{
                icon: lineSymbol,
                offset: '100%'
            }],
            strokeColor: '#FF0000',
            strokeOpacity: 0.5,
            strokeWeight: 2,
            map: map
        });
        return line;
    };

    // Creates a Info View for a given station
    var createInfoWindow = function(station) {
        // Use the model to
        var contentString = '<h3>' + station.name + '</h3>';
        var infoView = new google.maps.InfoWindow({
            content: contentString,
            maxWidth: 200
        });
        return infoView;
    };

    // Show the station on the map.
    var showStation = function(station) {
        var stationPos = new google.maps.LatLng(station.latitude, station.longitude);
        var marker = new google.maps.Marker({
            icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 5
            },
            position: stationPos,
            map : map
        });

        var infoWindow = createInfoWindow(station);
        var internalStation = station;
        var handler = google.maps.event.addListener(marker, 'click', function() {
            infoWindow.open(map, marker);
            model.setSelectedStation(internalStation);
        });
        markerClickHandlers.push(handler);
    };

    // Initially populate the
    this.showStations = function(stations) {
        for (var stationKey in stations) {
            showStation(stations[stationKey]);
        }
    }

    // Sets the map for all the Markers
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
    };

    // Register this view as an observer to the model.
    model.addObserver(this);

    var animateArrowFn = function(line, numTrips) {
        return function() {
            var count = 0;
            var interval = window.setInterval(function() {
                count = (count + 1) % numTrips;
                var icons = line.get('icons');
                icons[0].offset = (count / 2) + '%';
                line.set('icons', icons);
            }, 20);
            return interval;
        };
    };

    // This method gets called when the model notifies the observers
    this.update = function(arg) {
        // TODO Update the Map view by the current state of the model.
        var selectedStation = model.getSingleSelectedStation();
        var destinations = model.getCurrentDestinations();
        if (selectedStation != null && destinations != null) {
            model.getAllStations(function(result) {
                for (var id in destinations) {
                    var dest = result[id]; // Destination station.
                    var numTrips = destinations[id]; // Number of trips
                    if (numTrips < 50) continue;

                    var lineCoordinates = [
                        new google.maps.LatLng(selectedStation.latitude, selectedStation.longitude),
                        new google.maps.LatLng(dest.latitude, dest.longitude)
                    ];
                    var line = drawLine(lineCoordinates);
                    trips.push(line);

                    var interval = animateArrowFn(line, numTrips)();
                    // TODO load
                }
            });
        }
    }
}
