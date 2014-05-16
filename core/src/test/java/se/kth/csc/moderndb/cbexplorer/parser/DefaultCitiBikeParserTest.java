package se.kth.csc.moderndb.cbexplorer.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * Created by mhotan on 4/13/14.
 */
public class DefaultCitiBikeParserTest {

    @Test
    public void testParse() throws Exception {
        TestReader reader = new TestReader();
        DefaultCitiBikeParser parser = new DefaultCitiBikeParser(reader);

        // Check if the file exists
        // If no data files exists in core/src/main/resources/data then whats returned will be null.
        File defFile = parser.getDefaultResource();
        if (defFile == null)
            return;

        // If There are data files then make sure that data was inputted.
        long success = parser.parse();
        if (defFile.isDirectory() && defFile.listFiles().length == 0)
            Assert.assertEquals("Able to parse data set", 0, success);
        else
            Assert.assertNotEquals("Able to parse data set", 0, success);
        Assert.assertEquals("The count and number of trips return are equal", success, reader.count);
    }

    public static class TestReader implements CitiBikeReader {

        long count;

        public TestReader() {
            this.count = 0;
        }

        @Override
        public void addTrips(Collection<Trip> trips) {
            Assert.assertNotNull("Trips cannot be null", trips);
            Assert.assertNotEquals("Trips cannot be empty", 0, trips.size());
            count += trips.size();
        }
    }
}
