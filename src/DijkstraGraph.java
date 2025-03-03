// === CS400 File Header Information ===
// Name: Jaden Tan
// Email: <jtan94@wisc.edu email address>
// Group and Team: <your group name: 1804, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode
   * contains data about one specific path between the start node and another
   * node in the graph. The final node in this path is stored in its node
   * field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor
   * field (this field is null within the SearchNode containing the starting
   * node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost
   * SearchNode has the highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the
   * shortest path between the provided start and end locations. The
   * SearchNode that is returned by this method is represents the end of the
   * shortest path that is found: it's cost is the cost of that shortest path,
   * and the nodes linked together through predecessor references represent
   * all of the nodes along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found
   *                                or when either start or end data do not
   *                                correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    // checks if the start and end node given is null
    if (start == null || end == null) {
      throw new NoSuchElementException("Start or end node does not exist");
    }

    if (!this.containsNode(start) || !this.containsNode(end)) {
      throw new NoSuchElementException("Node not found in the graph");
    }

    PriorityQueue<SearchNode> queue = new PriorityQueue<>();
    MapADT<NodeType, Double> visit = new HashtableMap<>();

    Node startNode = this.nodes.get(start);
    // priority queue list
    queue.add(new SearchNode(startNode, 0, null));

    while (!queue.isEmpty()) {
      SearchNode current = queue.poll();
      if (current.node.data.equals(end)) {
        return current;
      }
      // checking if new path has lower cost
      if (visit.containsKey(current.node.data) && visit.get(current.node.data) <= current.cost) {
        continue;
      }
      visit.put(current.node.data, current.cost);

      for (Edge edge : current.node.edgesLeaving) {
        double newCost = current.cost + edge.data.doubleValue();
        Node successor = edge.successor;

        if (!visit.containsKey(successor.data) || visit.get(successor.data) > newCost) {
          queue.add(new SearchNode(successor, newCost, current));
        }
      }
    }
    throw new NoSuchElementException("No path");
  }

  /**
   * Returns the list of data values from nodes along the shortest path
   * from the node with the provided start value through the node with the
   * provided end value. This list of data values starts with the start
   * value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This
   * method uses Dijkstra's shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    SearchNode endNode = computeShortestPath(start, end);

    LinkedList<NodeType> path = new LinkedList<>();
    for (SearchNode node = endNode; node != null; node = node.predecessor) {
      // adding the nodes for the path
      path.addFirst(node.node.data);
    }

    return path;

  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest
   * path freom the node containing the start data to the node containing the
   * end data. This method uses Dijkstra's shortest path algorithm to find
   * this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {

    SearchNode endNode = computeShortestPath(start, end);
    return endNode.cost; // The cost of the shortest path
  }

  // this test tests the shortestPathData method
  @Test
  public void test1() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("D");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");
    graph.insertNode("F");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("M");
    graph.insertNode("B");
    graph.insertNode("A");
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("F", "G", 9.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "I", 2.0);
    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("B", "M", 3.0);
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("M", "F", 4.0);
    graph.insertEdge("G", "A", 4.0);
    graph.insertEdge("D", "A", 7.0);
    graph.insertEdge("A", "H", 7.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("I", "D", 1.0);


    List<String> path = graph.shortestPathData("D", "I");

    assertEquals(List.of("D", "G", "H", "I"), path,
            "wrong");
  }

  // this tests tests the shortestPathCost method
  @Test
  public void test2() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("D");
    graph.insertNode("G");
    graph.insertNode("H");
    graph.insertNode("L");
    graph.insertNode("F");
    graph.insertNode("I");
    graph.insertNode("E");
    graph.insertNode("M");
    graph.insertNode("B");
    graph.insertNode("A");
    graph.insertEdge("D", "G", 2.0);
    graph.insertEdge("D", "F", 4.0);
    graph.insertEdge("F", "G", 9.0);
    graph.insertEdge("G", "H", 9.0);
    graph.insertEdge("G", "L", 7.0);
    graph.insertEdge("H", "L", 2.0);
    graph.insertEdge("H", "I", 2.0);
    graph.insertEdge("I", "H", 2.0);
    graph.insertEdge("M", "E", 3.0);
    graph.insertEdge("M", "I", 4.0);
    graph.insertEdge("B", "M", 3.0);
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "M", 5.0);
    graph.insertEdge("M", "F", 4.0);
    graph.insertEdge("G", "A", 4.0);
    graph.insertEdge("D", "A", 7.0);
    graph.insertEdge("A", "H", 7.0);
    graph.insertEdge("H", "B", 6.0);
    graph.insertEdge("I", "D", 1.0);

    Double cost = graph.shortestPathCost("D", "H");
    assertEquals(11, cost, "wrong");
    List<String> path = graph.shortestPathData("D", "H");
    Assertions.assertTrue(path.contains("G"));


  }

  // this test tests that a NoSuchElementException is thrown when a node does not exist
  @Test
  public void test3() {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertEdge("A", "B", 2.0);
    graph.insertEdge("B", "C", 3.0);
    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("C", "A"), "wrong");
  }


}


