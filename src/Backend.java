import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Backend extends DijkstraGraph<String, Double> implements BackendInterface {
  private DijkstraGraph<String, Double> graph;

  public Backend(DijkstraGraph<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file.  If a graph was previously loaded, this
   * method should first delete the contents (nodes and edges) of the existing
   * graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  public void loadGraphData(String filename) throws IOException {

    try {

      if (filename == null) {
        throw new IOException("Filename cannot be null");
      }

      List<String> nodes = graph.getAllNodes();
      // removes the nodes if there are any
      for (String node : nodes) {
        graph.removeNode(node);
      }

      File campus = new File(filename);
      Scanner scan = new Scanner(campus);
      while (scan.hasNextLine()) {
        String line = scan.nextLine();

        if (line.contains("->") && line.contains("[seconds=")) {
          String[] parts = line.split("->");
          String source = parts[0].trim().replace("\"", "");
          String[] destinationPlace = parts[1].split("\\[seconds=");
          // destination location
          String destination = destinationPlace[0].trim().replace("\"", "");
          // time taken to get to destination
          Double seconds = Double.parseDouble(destinationPlace[1].replace("];",
                  ""));

          // insert the source if it is not in the graph
          if (!containsNode(source)) {
            this.graph.insertNode(source);
          }
          // insert the destination if it is not in the graph
          if (!containsNode(destination)) {
            this.graph.insertNode(destination);
          }
          // insert the edge
          this.graph.insertEdge(source, destination, seconds);
        }
      }


    } catch (IOException e) {
      System.out.println("File cannot be null");
    }
  }

  /**
   * Returns a list of all locations (node data) available in the graph.
   *
   * @return list of all location names
   */
  public List<String> getListOfAllLocations() {
    return graph.getAllNodes();
  }

  /**
   * Return the sequence of locations along the shortest path from
   * startLocation to endLocation, or an empty list if no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation
   * to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    // Validate input and check if nodes exist in the graph
    if (!graph.containsNode(startLocation) || !graph.containsNode(endLocation)) {
      throw new NoSuchElementException("Start or end node does not exist in the graph.");
    }

    // Attempt to find the shortest path using the graph's method
    List<String> shortestPath = graph.shortestPathData(startLocation, endLocation);

    // If no path exists, return an empty list
    if (shortestPath == null || shortestPath.isEmpty()) {
      return new ArrayList<>();
    }

    return shortestPath;
  }

  /**
   * Return the walking times in seconds between each two nodes on the
   * shortest path from startLocation to endLocation, or an empty list of no
   * such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along
   * the shortest path from startLocation to endLocation, or an empty
   * list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    // Get the sequence of locations on the shortest path
    List<String> locationStrings = findLocationsOnShortestPath(startLocation, endLocation);

    // Return an empty list if no path exists
    if (locationStrings == null || locationStrings.isEmpty()) {
      return new ArrayList<>();
    }

    List<Double> locationTimes = new ArrayList<>();

    // Calculate the walking times between consecutive nodes
    for (int i = 0; i < locationStrings.size() - 1; i++) {
      String start = locationStrings.get(i);
      String end = locationStrings.get(i + 1);

      // Use shortestPathCost to get the walking time between two nodes
      double time;
      try {
        time = graph.shortestPathCost(start, end);
      } catch (NoSuchElementException e) {
        throw new NoSuchElementException("Unable to calculate time between " + start + " and " + end + ".");
      }

      locationTimes.add(time);
    }

    return locationTimes;
  }

  /**
   * Returns the location can be reached from all of the specified start
   * locations in the shortest total time: minimizing the sum of the travel
   * times from each start locations.
   *
   * @param startLocations the list of locations to minimize travel time from
   * @return the location that can be reached in the shortest total time from
   * all of the specified start locations
   * @throws NoSuchElementException if there is no destination that can be
   *                                reached from all of the start locations, or if any of the start
   *                                locations does not exist within the graph
   */
  @Override
  public String getClosestDestinationFromAll(List<String> startLocations) throws NoSuchElementException {
    List<String> locations = graph.getAllNodes();

    // throw exception if there is no startLocation
    if (locations.isEmpty() || startLocations.isEmpty()) {
      throw new NoSuchElementException("Graph or start locations are empty.");
    }

    String closestDestination = null;
    double minTotalTime = Double.MAX_VALUE;

    for (String destination : locations) {
      double time = 0;
      // able to get to destination from startlocation
      boolean reach = true;

      for (String startLocation : startLocations) {
        try {
          time += graph.shortestPathCost(startLocation, destination);
        } catch (NoSuchElementException e) {
          reach = false;
          throw new NoSuchElementException("startLocation does not exist");
        }
      }
      // update closestDestination if another minTotalTime is found
      if (reach && time < minTotalTime) {
        minTotalTime = time;
        closestDestination = destination;
      }
    }

    if (closestDestination == null) {
      throw new NoSuchElementException("No reachable destination from all start locations.");
    }
    return closestDestination;
  }
}

