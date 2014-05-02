package se.kth.csc.moderndb.cbexplorer.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Default parser that reads data in from the resource directory.  This by default will
 * read all the trip files.
 *
 * Created by mhotan on 4/13/14.
 */
public class DefaultCitiBikeParser extends CitiBikeParser {

    // URL location of
    private static final URL dataDirectoryURL = DefaultCitiBikeParser.class.
            getClassLoader().getResource("data/");

    /**
     * Creates a Parser that is capable of forwarding parsed data back to the reader.
     *
     * @param reader CitiBike Reader class that handles the forwarding of trip data.
     */
    public DefaultCitiBikeParser(CitiBikeReader reader) {
        super(reader);
    }

    /**
     * Creates a Parser that is capable of forwarding parsed data back to the reader.
     *
     * @param reader CitiBike Reader class that handles the forwarding of trip data.
     * @param tripBufferSize Buffersize of tripdata stored in memory.  In units of TripData.
     */
    public DefaultCitiBikeParser(CitiBikeReader reader, int tripBufferSize) {
        super(reader, tripBufferSize);
    }

    /**
     * Parses the data stored in the resource directory.
     * @return The number of successful reads.
     */
    public long parse() throws IOException, ParseException {
        long startTime = Calendar.getInstance().getTime().getTime();

        long reads;
        try {
            reads = parse(new File(dataDirectoryURL.toURI()));
        } catch (URISyntaxException e) {
            // The url should always be valid an exists.
            throw new RuntimeException(e);
        }
        long endTime = Calendar.getInstance().getTime().getTime();
        long millis = endTime - startTime;
        String duration = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        System.out.println(reads + " Number of total reads in " + duration);
        return reads;
    }
}
