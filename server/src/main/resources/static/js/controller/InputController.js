/**
 * Created by mhotan on 5/12/14.
 */

var InputController = function (view, model) {

    this.update = function() {
        // TODO Set the contents of the input based off the current state of the model

    }

    // All listening classes that must implement
    // all the menu options controller listener functions.
    var listeners = [];

    // Add the listener.
    this.addListener = function(listener) {
        listeners.push(listener);
    }

    // Notify the application listeners some kind of interaction occured
    var notifyListeners = function(input) {
        // TODO iterate through all the listeners.
    }

    this.update();
}