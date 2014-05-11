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
        getStationDestinations: function (station, callback) {
            $.getJSON(api_path + "/stations/" + station + "/destinations", callback);
        },
        getDistanceBetweenStations: function (station1, station2, callback) {
            $.getJSON(api_path + "/stations/distance/" + station1 + "/" + station2);
        }
    };
})(jQuery);