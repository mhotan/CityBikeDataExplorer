package se.kth.csc.moderndb.cbexplorer;

import se.kth.csc.moderndb.cbexplorer.reader.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.reader.STDBCityBikeReader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Jeannine on 28.04.14.
 */
public class MainTDB {

    public static void main(String [] args) {
        STDBCityBikeReader reader = new STDBCityBikeReader();
        CitiBikeParser parser = new CitiBikeParser(reader);

        File file = new File("/Users/Jeannine/git/CityBikeDataExplorer/core/src/main/resources/2014-02 - Citi Bike trip data.csv");

        try {
            parser.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
