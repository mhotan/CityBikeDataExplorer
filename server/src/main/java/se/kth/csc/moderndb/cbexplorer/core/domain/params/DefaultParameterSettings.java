package se.kth.csc.moderndb.cbexplorer.core.domain.params;

import se.kth.csc.moderndb.cbexplorer.core.domain.range.ShortRange;

/**
 * Class just used to send the default parameter settings.
 *
 * Created by mhotan on 5/12/14.
 */
public class DefaultParameterSettings {

//    private List<Bike> bikes;

    private TemporalParameters temporalParameters;

    private ShortRange birthRange;

//    private List<Station> stations;

    public DefaultParameterSettings() {
    }

    public DefaultParameterSettings(TemporalParameters temporalParameters,
                                    ShortRange birthRange) {
        if (temporalParameters == null || birthRange == null)
            throw new NullPointerException("Can't have null Default Parameters");
//        this.bikes = bikes;
        this.temporalParameters = temporalParameters;
        this.birthRange = birthRange;
//        this.stations = stations;
    }

//    public List<Bike> getBikes() {
//        return bikes;
//    }

    public TemporalParameters getTemporalParameters() {
        return temporalParameters;
    }

    public ShortRange getBirthRange() {
        return birthRange;
    }

//    public List<Station> getStations() {
//        return stations;
//    }
}
