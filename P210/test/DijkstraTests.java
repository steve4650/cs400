import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraTests {

  /**
   * Rest the result of the lecture example.
   */
  @Test
  public void test1() {
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

    Assertions.assertEquals(12, graph.shortestPathCost("A", "D"));
    Assertions.assertEquals("A", graph.shortestPathNodes("A", "D").get(0));
    Assertions.assertEquals("C", graph.shortestPathNodes("A", "D").get(1));
    Assertions.assertEquals("E", graph.shortestPathNodes("A", "D").get(2));
    Assertions.assertEquals("D", graph.shortestPathNodes("A", "D").get(3));
  }

}
