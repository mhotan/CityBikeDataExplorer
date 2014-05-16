package se.kth.csc.moderndb.cbexplorer;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeReader;
import se.kth.csc.moderndb.cbexplorer.parser.DefaultCitiBikeParser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by mhotan on 5/1/14.
 */
public class DatabasePopulator {

    private DatabasePopulator() {
//        Cannot Instantiate
    }

    /**
     * Method that handles the population of
     * @param args Unused arguments.
     */
    public static void main(String[] args) throws ParseException, IOException {
        if (args.length != 1) {
            String error =  "Illegal argument signature";
            System.err.println(error);
            System.err.println("Correct argument signature: <path to citibike data>");
            throw new IllegalArgumentException(error);
        }

        File f = new File(args[0]);
        if (!f.exists()) {
            throw new IllegalArgumentException("File does not exists at " + args[0]);
        }

        // Delete the old database
        File db = new File(DatabaseConstants.DATABASE_PATH);
        if (db.exists()) {
            db.delete();
        }

        BatchInserter inserter = BatchInserters.inserter(DatabaseConstants.DATABASE_PATH);
        CitiBikeReader reader = new CitiBikeBatchReader(inserter);
        DefaultCitiBikeParser parser = new DefaultCitiBikeParser(reader);
        try {
            parser.parse(f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inserter.shutdown();
        }
    }

}
