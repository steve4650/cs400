import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraTests {

  /** Test the result of the lecture example. */
  @Test
  public void test1() {
    // Build the lecture graph
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addNode("D");
    graph.addNode("E");
    graph.addNode("F");
    graph.addNode("G");
    graph.addNode("H");
    graph.addEdge("A", "B", 8);
    graph.addEdge("A", "C", 3);
    graph.addEdge("B", "F", 2);
    graph.addEdge("C", "D", 9);
    graph.addEdge("C", "E", 2);
    graph.addEdge("D", "B", 3);
    graph.addEdge("E", "D", 7);
    graph.addEdge("E", "F", 7);
    graph.addEdge("E", "H", 4);
    graph.addEdge("G", "A", 4);
    graph.addEdge("G", "H", 3);
    graph.addEdge("H", "F", 3);

    // Test the cost and nodes along the path
    Assertions.assertEquals(12, graph.shortestPathCost("A", "D"));
    Assertions.assertEquals("A", graph.shortestPathNodes("A", "D").get(0));
    Assertions.assertEquals("C", graph.shortestPathNodes("A", "D").get(1));
    Assertions.assertEquals("E", graph.shortestPathNodes("A", "D").get(2));
    Assertions.assertEquals("D", graph.shortestPathNodes("A", "D").get(3));
  }

  /** Test non-existent node behavior */
  @Test
  public void test2() {
    // Build the lecture graph
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addEdge("A", "B", 8);
    graph.addEdge("A", "C", 3);

    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("A", "D"));
    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathNodes("A", "D"));
  }

  /** Test non-existent node path behavior */
  @Test
  public void test3() {
    // Build the lecture graph
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addNode("D");
    graph.addEdge("A", "B", 8);
    graph.addEdge("A", "C", 3);
    graph.addEdge("C", "D", 21);

    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("D", "A"));
    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathNodes("C", "B"));
  }
}
