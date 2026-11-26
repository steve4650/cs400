import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This is the interface that a backend developer will implement, so that a frontend developer's
 * code can make use of this functionality. It makes use of a GraphADT to perform shortest path
 * computations.
 */
public interface BackendInterface {

  /*
   * Implementing classes should support the constructor below.
   * @param graph object to store the backend's graph data
   */
  // public Backend(GraphADT<String,Double> graph);

  /**
   * Loads graph data from a dot file. If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  public void loadDotFile(String filename) throws IOException;

  /**
   * Returns a list of all locations in the graph.
   *
   * @return list of all location names
   */
  public List<String> getAllLocations();

  /**
   * Return the sequence of locations along the shortest path from start to end, or an empty list if
   * no such path exists.
   *
   * @param start the start of the path
   * @param end the end of the path
   * @return a list with the nodes along the shortest path from start to end, or an empty list if no
   *     such path exists
   */
  public List<String> findShortestPathLocations(String start, String end);

  /**
   * Return the times in seconds between each two nodes on the shortest path from start to end, or
   * an empty list if no such path exists.
   *
   * @param start the start of the path
   * @param end the end of the path
   * @return a list with the times in seconds between two nodes along the shortest path from start
   *     to end, or an empty list if no such path exists
   */
  public List<Double> findShortestPathTimes(String start, String end);

  /**
   * Returns the list of locations that can be reached when starting from the provided start, and
   * travelling maxTime in seconds.
   *
   * @param start the location to find the reachable locations from
   * @param maxTime is the maximum time it can take to get from from the start to report a location
   * @return the list of locations that can be reached from start in maxTime or fewer seconds
   * @throws NoSuchElementException if start does not exist
   */
  public List<String> getReachableWithin(String start, double maxTime)
      throws NoSuchElementException;
}
