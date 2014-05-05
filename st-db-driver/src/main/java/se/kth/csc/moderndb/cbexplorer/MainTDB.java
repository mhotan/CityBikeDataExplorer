package se.kth.csc.moderndb.cbexplorer;

import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.STDBCityBikeReader;
import se.kth.csc.moderndb.cbexplorer.queries.StationQuery;

import java.io.File;

/**
 * Created by Jeannine on 28.04.14.
 */
public class MainTDB {

    public static void main(String [] args) {
        STDBCityBikeReader reader = new STDBCityBikeReader();
        CitiBikeParser parser = new CitiBikeParser(reader);

        File file = new File("/Users/Jeannine/git/CityBikeDataExplorer/core/src/main/resources/2014-02 - Citi Bike trip data.csv");

        StationQuery stationQuery = new StationQuery();
        stationQuery.giveFullStationInformationAboutAllStations();
        stationQuery.giveFullStationInformationAboutStationNamed("Cherry St");
        /*try {
            parser.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }
}
