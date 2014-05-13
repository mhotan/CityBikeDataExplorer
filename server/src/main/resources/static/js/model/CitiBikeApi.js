var CitiBikeApi = (function ($) {
    var api_path = "http://localhost:8080/api";
    return {
        findAllBikes: function (callback) {
            $.getJSON(api_path + "/bikes", callback);
        },
        getBikeTripCount: function (bike, callback) {
            $.getJSON(api_path + "/bikes/" + bike + "/tripCount", callback);
        },
        getBikeTrips: function (bike, startTime, endTime, callback) {
            if (startTime instanceof Date) startTime = startTime.getTime();
            if (endTime instanceof Date) endTime = endTime.getTime();
            $.getJSON(api_path + "/bikes/" + bike + "/trips/" + startTime + "/" + endTime, callback);
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
            $.getJSON(api_path + "/stations/distance/" + station1 + "/" + station2, callback);
        },
        getTimeRange: function(callback) {
            $.getJSON(api_path + "/trips/timeRange", callback);
        },
        // Pull the default parameters from
        getDefaultParameters: function(callback) {
            $.getJSON(api_path + "/parameters/default", callback);
        }
    };
})(jQuery);