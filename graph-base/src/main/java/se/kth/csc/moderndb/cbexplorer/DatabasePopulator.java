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

    private static final String DB_PATH = "target/test.db";

    private DatabasePopulator() {
//        Cannot Instantiate
    }

    /**
     * Method that handles the population of
     * @param args Unused arguments.
     */
    public static void main(String[] args) throws ParseException, IOException {
        File f = null;
        if (args.length != 0) {
            String path = args[0];
            f = new File(path);
            if (!f.exists()) {
                throw new IllegalArgumentException("File does not exists at " + path);
            }
        }

        // Delete the old database
        File db = new File(DB_PATH);
        if (db.exists()) {
            db.delete();
        }

        BatchInserter inserter = BatchInserters.inserter(DB_PATH);
        CitiBikeReader reader = new CitiBikeBatchReader(inserter);
        DefaultCitiBikeParser parser = new DefaultCitiBikeParser(reader);
        if (f == null) {
            parser.parse();
        } else {
            parser.parse(f);
        }

        // Shutdown the inserter.
        inserter.shutdown();
    }

}
