import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This is a placeholder for the fully working Backend that will be developed
 * by one of your teammates this week and then integrated with your role code
 * in a future week.  It is designed to help develop and test the functionality
 * of your own Frontend role code this week.  Note the limitations described
 * below.
 */
public class Backend_Placeholder implements BackendInterface {

    // Presumably this placeholder is using a placeholder graph that is itself
    // not fully functional.
    // This field is public to allow test code to access it.
    public GraphADT<String,Double> graph;
    public List<String> nodes = new ArrayList<>(List.of("Union South",
        "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
    public Backend_Placeholder(GraphADT<String,Double> graph) {
        this.graph = graph;
    }

    // this method adds a single extra location to the graph when called
    public void loadDotFile(String filename) throws IOException {
        graph.addNode("Alpha_Centauri_A");
    }
    
    public List<String> getAllLocations() {
        return nodes;
    }

    public List<String> findShortestPathLocations(String start, String end) {
        return graph.shortestPathNodes(start,end);
    }

    // returns list of increasing values
    public List<Double> findShortestPathTimes(String start, String end) {
        List<String> locations = graph.shortestPathNodes(start,end);
        List<Double> times = new ArrayList<>();
        for(int i=0;i<locations.size();i++) times.add(i+1.0);
        return times;
    }

    // always returns entire list of locations
    public List<String> getReachableWithin(String start, double travelTime) throws NoSuchElementException {
        return nodes;
    }
}
