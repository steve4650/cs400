import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** This class extends the DijkstraGraph class to run submission checks on it. */
public class P210SubmissionChecker extends DijkstraGraph<Integer, Integer> {

  /**
   * Creates a graph with 4 nodes and 7 edges and checks the path that returned between two of the
   * nodes based on the shortestPathData and shortestPathCost methods.
   */
  @Test
  public void submissionCheckerSmallGraph() {
    DijkstraGraph<Integer, Integer> graph = new DijkstraGraph<>();
    graph.addNode(1);
    graph.addNode(6);
    graph.addNode(11);
    graph.addNode(3);

    graph.addEdge(11, 1, 3);
    graph.addEdge(6, 11, 7);
    graph.addEdge(1, 11, 5);
    graph.addEdge(6, 6, 1);
    graph.addEdge(1, 6, 4);
    graph.addEdge(6, 1, 6);
    graph.addEdge(6, 3, 9);

    Assertions.assertEquals(7, graph.shortestPathCost(6, 11));
    Assertions.assertEquals("[6, 11]", graph.shortestPathNodes(6, 11).toString());
  }

  /**
   * Check that a NoSuchElementException is thrown if a path from start to end node does not exist.
   */
  @Test
  public void submissionCheckerNoPath() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");

    graph.addEdge("A", "B", 2);
    graph.addEdge("B", "C", 3);

    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("B", "A"));
  }

  /** Check that a NoSuchElementException is thrown if start node does not exist in graph. */
  @Test
  public void submissionCheckerNoNode() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");

    graph.addEdge("A", "B", 2);
    graph.addEdge("B", "C", 3);

    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathNodes("X", "A"));
  }
}
