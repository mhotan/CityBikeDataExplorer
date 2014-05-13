package se.kth.csc.moderndb.cbexplorer;

import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;
import se.kth.csc.moderndb.cbexplorer.parser.STDBCityBikeReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Created by Jeannine on 28.04.14.
 */
public class MainTDB {

    public static void main(String [] args) throws FileNotFoundException, SQLException {
       if (args.length != 1) {
            String error =  "Illegal argument signature";
            System.err.println(error);
            System.err.println("Correct argument signature: <path to citibike data>");
            throw new IllegalArgumentException(error);
        }

        File data = new File(args[0]);
        if (!data.exists()) {
            throw new FileNotFoundException("File does not exists at " + args[0]);
        }

        STDBCityBikeReader reader = new STDBCityBikeReader();
        CitiBikeParser parser = new CitiBikeParser(reader);

        try {
            parser.parse(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // TODO Refactor connection handling.
            reader.close();
        }
    }
}
