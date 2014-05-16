package se.kth.csc.moderndb.cbexplorer.parser;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.csc.moderndb.cbexplorer.core.data.Bike;
import se.kth.csc.moderndb.cbexplorer.core.data.Station;
import se.kth.csc.moderndb.cbexplorer.core.data.Trip;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Parser class for extracting CitiBike data from compressed files.  Forwards
 * parsed information to a CitiBikeReader.  The Reader can then structure the data
 *
 *
 * Created by mhotan on 4/13/14.
 */
public class CitiBikeParser {

    private static final Logger LOG = LoggerFactory.getLogger(CitiBikeParser.class);

            // Indexes for each line within the csv file
    private static final int TRIP_DURATION_INDEX = 0;
    private static final int START_TIME_INDEX = 1;
    private static final int END_TIME_INDEX = 2;
    private static final int START_STATION_ID_INDEX = 3;
    private static final int START_STATION_NAME_INDEX = 4;
    private static final int START_STATION_LATITUDE_INDEX = 5;
    private static final int START_STATION_LONGITUDE_INDEX = 6;
    private static final int END_STATION_ID_INDEX = 7;
    private static final int END_STATION_NAME_INDEX = 8;
    private static final int END_STATION_LATITUDE_INDEX = 9;
    private static final int END_STATION_LONGITUDE_INDEX = 10;
    private static final int BIKE_ID_INDEX = 11;
    private static final int USER_TYPE_INDEX = 12;
    private static final int BIRTH_YEAR_INDEX = 13;
    private static final int GENDER_INDEX = 14;

    // CSV Parameters
    private static final char CSV_SEPERATOR = ',';
    private static final char CSV_QUOTE_CHAR = '"';

    // The default number of
    // TODO Validate this quantity to work on all systems.
    private static final int DEFAULT_TRIPDATA_BUFFERSIZE = 5000;

    /**
     * Reader that reads and interprets raw data.
     */
    private final CitiBikeReader reader;

    /**
     * The number of trips that can be stored in a collection.
     */
    private final int maxNumberOfTrips;

    /**
     * Creates a Parser that can interpret Citibike raw data.
     *
     * @param reader Reader that is notified of new raw data.
     */
    public CitiBikeParser(CitiBikeReader reader) {
        this(reader, DEFAULT_TRIPDATA_BUFFERSIZE);
    }

    public CitiBikeParser(CitiBikeReader reader, int maxNumberOfTrips) {
        if (reader == null)
            throw new NullPointerException(getClass().getSimpleName() + "(): " +
                    "Null CitiBikeParser");
        this.reader = reader;
        this.maxNumberOfTrips = Math.max(maxNumberOfTrips, 1);
    }

    /**
     * Parses a file for all CitiBike Data.
     *
     * @param file File to parse the data for.
     * @return The number of successful reads
     * @throws java.io.IOException Unable to read file
     * @throws java.text.ParseException Unable to process the date time field
     */
    public long parse(File file) throws IOException, ParseException {
        Collection<Reader> readers = getFileReaders(file);
        long numberRead = 0;
        for (Reader reader : readers) {
            numberRead += parse(reader);
        }
        return numberRead;
    }

    /**
     * Parse a single BufferedReader for CitiBike Data.
     * @param reader The reader to process for data.
     * @return Number of lines read.
     */
    private long parse(Reader reader) throws IOException, ParseException {

        // The buffer to store the number of trips
        List<Trip> tripBuffer = new ArrayList<Trip>(maxNumberOfTrips);
        long numberOfSuccesses = 0;

        // Date formatter for converting String to appropiate date.
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        CSVReader csvReader = new CSVReader(reader, CSV_SEPERATOR, CSV_QUOTE_CHAR);
        String[] row = null;
        while ((row = csvReader.readNext()) != null) {

            // Check if we are at a header row.
            // Header rows do not have a number.
            String bikeIDString = row[BIKE_ID_INDEX];
            if (!StringUtils.isNumeric(bikeIDString)) continue;

            // Extract the nest data items.
            Bike bikeData = new Bike(Long.valueOf(bikeIDString));
            Station startData = new Station(Long.valueOf(row[START_STATION_ID_INDEX]),
                    row[START_STATION_NAME_INDEX], Double.valueOf(row[START_STATION_LONGITUDE_INDEX]),
                    Double.valueOf(row[START_STATION_LATITUDE_INDEX]));
            Station endData = new Station(Long.valueOf(row[END_STATION_ID_INDEX]), row[END_STATION_NAME_INDEX],
                    Double.valueOf(row[END_STATION_LONGITUDE_INDEX]), Double.valueOf(row[END_STATION_LATITUDE_INDEX]));

            // Parse birth year, which may be a numeric string or "\N".
            short birthYear;
            try {
                birthYear = Short.valueOf(row[BIRTH_YEAR_INDEX]);
            } catch (NumberFormatException e) {
                birthYear = 0;

            }

            // Parse the duration.
            int duration;
            try {
                duration = Integer.valueOf(row[TRIP_DURATION_INDEX]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Duration must be an integer not " + row[TRIP_DURATION_INDEX]);
            }

            // Parse the data objects.
            Date startTime = format.parse(row[START_TIME_INDEX]);
            Date endTime = format.parse(row[END_TIME_INDEX]);

            Trip trip = new Trip(
                    startTime,
                    endTime,
                    duration,
                    row[USER_TYPE_INDEX],
                    birthYear,
                    Short.valueOf(row[GENDER_INDEX]),
                    startData,
                    endData,
                    bikeData
            );
            tripBuffer.add(trip);

            // Check if the buffer is full
            // If so notify the cbexplorer reader
            if (tripBuffer.size() >= this.maxNumberOfTrips) {
                numberOfSuccesses += tripBuffer.size();
                this.reader.addTrips(tripBuffer);
                tripBuffer.clear();
                LOG.debug(numberOfSuccesses + " total trips processed \r");
            }
        }

        // Check if there is any residual trips in the buffer
        if (!tripBuffer.isEmpty()) {
            numberOfSuccesses += tripBuffer.size();
            this.reader.addTrips(tripBuffer);
            LOG.debug(numberOfSuccesses + " total trips processed \r");
        }
        LOG.debug("Data Loaded!");

        // Close teh CSV reader.
        csvReader.close();

        return numberOfSuccesses;
    }

    /**
     * Recursively attempts to find all the buffered readers that pertain
     * to an csv file.  If the argument file is not a directory then it attempts to
     * parse the file as a single file for data.  If file is not a directory then
     *  the collection returned will be an element of one.
     *
     * @param file File to recursively search or create reader around.
     * @return Collection of all the Buffered Readers
     * @throws IOException There was an error accessing one of the files.
     */
    private Collection<Reader> getFileReaders(File file) throws IOException {
        Set<Reader> files = new HashSet<Reader>();
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File childFile: childFiles) {
                files.addAll(getFileReaders(childFile));
            }
        } else {
            // Check if file is of correct type.
            if (file.getPath().endsWith(".gz") || file.getPath().endsWith(".zip") || file.getPath().endsWith(".csv"))
                files.add(getFileReader(file));
        }
        return files;
    }

    /**
     * Return the file reader for a single file.
     *
     * @param file Single file to create Buffered Reader with.
     * @return The buffered reader for this file.  Or null if this file is a directory or compressed directory that is empty
     * @throws IOException Unable to read the single file.
     */
    private Reader getFileReader(File file) throws IOException {
        if (file.isDirectory())
            throw new IllegalArgumentException("File cannot be a directory.");

        BufferedReader fileReader;
        // support compressed files
        if (file.getPath().endsWith(".gz")) {

            fileReader = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(new FileInputStream(file))));
        } else if (file.getPath().endsWith(".zip")) {

            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            // If there are no more elements then ignore the this file
            if (!entries.hasMoreElements()) {
                return null;
            }
            ZipEntry entry = entries.nextElement();
            fileReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
        } else if (file.getPath().endsWith(".csv")) {
            fileReader = new BufferedReader(new FileReader(file));
        } else {
            throw new IllegalArgumentException("Illegal file format to read for file " + file.getName());
        }
        return fileReader;
    }

}
