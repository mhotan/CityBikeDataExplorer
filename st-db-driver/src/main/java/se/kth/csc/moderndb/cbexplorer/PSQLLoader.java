package se.kth.csc.moderndb.cbexplorer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import se.kth.csc.moderndb.cbexplorer.parser.CitiBikeParser;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Class that provides a main method that loads data from a specific
 *  uri path.
 *
 * Created by Jeannine on 28.04.14.
 */
public class PSQLLoader {

    public static void main(String [] args)
            throws SQLException, IOException, ParseException {
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

        // Extract the Spring Configuration and data source..
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JDBCConfiguration.class);
        DataSource dataSource = ctx.getBean(DataSource.class);

        // Use the data source to read in the data.
        STDBCityBikeReader reader = new STDBCityBikeReader(dataSource);
        CitiBikeParser parser = new CitiBikeParser(reader);

//        try {
        parser.parse(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            reader.close();
//        }
    }
}
