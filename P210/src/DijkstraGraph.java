import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referenced by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * <p>SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode pred;

    public SearchNode(Node startNode) {
      this.node = startNode;
      this.cost = 0;
      this.pred = null;
    }

    public SearchNode(SearchNode pred, Edge newEdge) {
      this.node = newEdge.succ;
      this.cost = pred.cost + newEdge.data.doubleValue();
      this.pred = pred;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost) return +1;
      if (cost < other.cost) return -1;
      return 0;
    }
  }

  /** Constructor that sets the map that the graph uses. */
  public DijkstraGraph() {
    super(new PlaceholderMap<>());
  }

  /**
   * Add a new directed edge with positive edges weight into the graph. Or if an edge between pred
   * and succ already exists, update the data stored in that edge with the new weight.
   *
   * @param pred is the data item contained in the new edge's predecesor node
   * @param succ is the data item contained in the new edge's successor node
   * @param weight is the positive valued data item stored in the new edge
   * @return true if the edge could be inserted or updated, or false if the pred or succ data are
   *     not found in any graph nodes or if the weight specified was not a positive value >0.0
   */
  @Override
  public boolean addEdge(NodeType pred, NodeType succ, EdgeType weight) {
    if (weight.doubleValue() < 0) return false;
    return super.addEdge(pred, succ, weight);
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method represents
   * the end of the shortest path that is found: it's cost is the cost of that shortest path, and
   * the nodes linked together through predecessor references represent all of the nodes along that
   * shortest path (ordered from end to start).
   *
   * @param start the starting node for the path
   * @param end the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException if either the start or the end node cannot be found, or there is
   *     no path from start node to end node
   * @throws NullPointerException if the start or end node are null
   */
  protected SearchNode computeShortestPath(Node start, Node end) {
    return null;
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shortest path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   * @throws NoSuchElementException if either the start or end node cannot be found in the graph, or
   *     if there is no directed path from the start node to the end node
   */
  @Override
  public List<NodeType> shortestPathNodes(NodeType start, NodeType end) {
    Node startNode = nodes.get(start);
    Node endNode = nodes.get(end);

    SearchNode path = computeShortestPath(startNode, endNode);
    List<NodeType> result = new LinkedList<>();
    for (SearchNode current = path; current != null; current = current.pred) {
      result.add(current.node.data);
    }
    return result.reversed();
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path from the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   * @throws NoSuchElementException if either the start or end node cannot be found in the graph, or
   *     if there is no directed path from the start node to the end node
   */
  @Override
  public double shortestPathCost(NodeType start, NodeType end) {
    Node startNode = nodes.get(start);
    Node endNode = nodes.get(end);
    return computeShortestPath(startNode, endNode).cost;
  }
}
