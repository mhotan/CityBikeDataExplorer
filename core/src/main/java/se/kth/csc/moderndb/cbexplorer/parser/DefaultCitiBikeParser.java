package se.kth.csc.moderndb.cbexplorer.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

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
        try {
            return parse(new File(dataDirectoryURL.toURI()));
        } catch (URISyntaxException e) {
            // The url should always be valid an exists.
            throw new RuntimeException(e);
        }
    }
}
