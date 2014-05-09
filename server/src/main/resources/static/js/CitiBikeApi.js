var CitiBikeApi = (function ($) {
    var api_path = "/api";
    return {
        getAllBikes: function (callback) {
            $.getJSON(api_path + "/bikes", callback);
        },
        getAllStations: function (callback) {
            $.getJSON(api_path + "/stations", callback);
        }
    };
})(jQuery);