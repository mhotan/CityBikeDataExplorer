package se.kth.csc.moderndb.cbexplorer.parser;

import org.junit.Assert;
import se.kth.csc.moderndb.cbexplorer.parser.data.TripData;

import java.util.Collection;

/**
 * Created by mhotan on 4/13/14.
 */
public class DefaultCitiBikeParserTest {

    /*@Test
    public void testParse() throws Exception {
        TestReader parser = new TestReader();
        DefaultCitiBikeParser parser = new DefaultCitiBikeParser(parser);
        long success = parser.parse();
        Assert.assertNotEquals("Able to parse data set", 0, success);
        Assert.assertEquals("The count and number of trips return are equal", success, parser.count);
    }*/

    public static class TestReader implements CitiBikeReader {

        long count;

        public TestReader() {
            this.count = 0;
        }

        @Override
        public void addTrips(Collection<TripData> trips) {
            Assert.assertNotNull("Trips cannot be null", trips);
            Assert.assertNotEquals("Trips cannot be empty", 0, trips.size());
            count += trips.size();
        }
    }
}
