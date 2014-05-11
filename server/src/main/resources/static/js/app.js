/**
 * Basic controller class that mitigates the interaction between user interaction
 * and
 *
 * Created by mhotan on 5/11/14.
 */
$(document).ready(function() {

    // Citibike Model.
    var model = new CitiBikeModel();

    // Initialize
    var map = new google.maps.Map(document.getElementById('map_canvas'), {
        zoom: 13,
        center: new google.maps.LatLng(40.7192,-73.95)
    });

    // The Map view.
    var mapView = new CitiBikeMapView(map, model);
    var mapController = new MapController(mapView, model);

});