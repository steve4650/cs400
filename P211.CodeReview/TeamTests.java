import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests to test Backend implementations for code review.
 */
public class TeamTests {

    /**
     * Test the getAllLocations methods. Does it return the expected locations?
     */
    @Test
    public void test1() {
        BackendInterface backend = new Backend(new Graph_Placeholder());
        List<String> locations = backend.getAllLocations();
        assertEquals(3, locations.size());
        assertTrue(locations.contains("Union South"));
        assertTrue(locations.contains("Computer Sciences and Statistics"));
        assertTrue(locations.contains("Weeks Hall for Geological Sciences"));
    }

    /**
     * Test the findShortestPathLocations method. Does it return the expected path for Union South --> CS?
     */
    @Test
    public void test2() {
        BackendInterface backend = new Backend(new Graph_Placeholder());
        List<String> path = backend.findShortestPathLocations("Union South", "Computer Sciences and Statistics");
        assertEquals(2, path.size());
        assertEquals("Union South", path.get(0));
        assertEquals("Computer Sciences and Statistics", path.get(1));
    }

    /**
     * Test the findShortestPathTimes method. Does it return the expected travel times for Union South --> CS?
     */
    @Test
    public void test3() {
        BackendInterface backend = new Backend(new Graph_Placeholder());
        List<Double> times = backend.findShortestPathTimes("Union South", "Computer Sciences and Statistics");
        assertEquals(1, times.size());
        assertEquals(1.0, times.get(0));
    }
}
