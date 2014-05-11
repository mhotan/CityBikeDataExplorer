var CitiBikeApi = (function ($) {
    var api_path = "/api";
    return {
        findAllBikes: function (callback) {
            $.getJSON(api_path + "/bikes", callback);
        },
        getBikeTripCount: function (bike, callback) {
            $.getJSON(api_path + "/bikes/" + bike + "/tripCount", callback);
        },
        findAllStations: function (callback) {
            $.getJSON(api_path + "/stations", callback);
        },
        findStationByName: function (name, callback) {
            $.getJSON(api_path + "/stations/byName/" + encodeURIComponent(name), callback);
        },
        findStationPairsWithDistance: function(distance, callback) {
            $.getJSON(api_path + "/stations/pairsWithDistance/" + distance, callback);
        },
        getStationDestinations: function (station, callback) {
            $.getJSON(api_path + "/stations/" + station + "/destinations", callback);
        }
    };
})(jQuery);