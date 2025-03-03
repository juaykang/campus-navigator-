import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *  Frontend class with methods that return strings with HTML fragments for different request options.
 */
public class Frontend implements FrontendInterface {
  // Variable to store the backend that will be used in this class
  private BackendInterface backend;
  /**
   * Constructor that saves the backend.
   *
   * @param backend is used for shortest path computations
   */
  public Frontend(BackendInterface backend) {
    this.backend = backend;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a text input field with the id="start", for the start location
   * - a text input field with the id="end", for the destination
   * - a button labelled "Find Shortest Path" to request this computation
   * Ensure that these text fields are clearly labelled, so that the user
   * can understand how to use them.
   * @return an HTML string that contains input controls that the user can
   *         make use of to request a shortest path computation
   */
  public String generateShortestPathPromptHTML() {
    String html = "";
    html += "<p>Start location: </p>\n";
    html += "<input type=\"text\" id=\"start\"></input><br>\n";
    html += "<p>Destination: </p>\n";
    html += "<input type=\"text\" id=\"end\"></input><br>\n";
    html += "<input type=\"button\" value=\"ShortestPathData\"></input><br>\n";
    return html;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a paragraph (p) that describes the path's start and end locations
   * - an ordered list (ol) of locations along that shortest path
   * - a paragraph (p) that includes the total travel time along this path
   * Or if there is no such path, the HTML returned should instead indicate
   * the kind of problem encountered.
   * @param start is the starting location to find a shortest path from
   * @param end is the destination that this shortest path should end at
   * @return an HTML string that describes the shortest path between these
   *         two locations
   */
  public String generateShortestPathResponseHTML(String start, String end) {
    String html = "";
    // Adding paragraph that describes the path's start and end locations
    html += "<p>Start location is \"" + start + "\" and end location is \"" + end + "\".</p>\n";
    // Adding ordered list (ol) of locations along that shortest path
    html += "<ol>\n";
    for (String s : backend.findLocationsOnShortestPath(start, end)) {
      html += "  <li>" + s + "</li>\n";
    }
    html += "</ol>\n";
    // Adding paragraph (p) that includes the total travel time along this path
    try {
      Double total = this.totalTime(start, end);
      html += "<p>Total travel time: " + total + ".</p>\n";
    } catch (NoSuchElementException e) {
      html += "<p>No path exists.</p>\n";
    }
    return html;
  }

  /**
   * Helper method to calculate the total travel time along the shortest path from the start to the end location.
   * If there is no such path, exception is thrown.
   *
   * @param start is the starting location to find a shortest path from
   * @param end is the destination that this shortest path should end at
   * @return total travel time
   * @throws NoSuchElementException when there is no path
   */
  private Double totalTime(String start, String end) throws NoSuchElementException {
    if (start.equals(end)) return 0.0;
    Double total = 0.0;
    List<Double> pathTimes = backend.findTimesOnShortestPath(start, end);
    if (pathTimes.isEmpty()) throw new NoSuchElementException("No path exists.");
    for (Double t : pathTimes) {
      total += t;
    }
    return total;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a text input field with the id="from", for the start locations
   * - a button labelled "Closest From All" to submit this request
   * Ensure that this text field is clearly labelled, so that the user
   * can understand that they should enter a comma separated list of as
   * many locations as they would like into this field
   * @return an HTML string that contains input controls that the user can
   *         make use of to request a ten closest destinations calculation
   */
  public String generateClosestDestinationsFromAllPromptHTML() {
    String html = "";
    html += "<p>Start locations (comma separated list): </p>\n";
    html += "<input type=\"text\" id=\"from\"></input><br>\n";
    html += "<input type=\"button\" value=\"ShortestPathData\"></input><br>\n";
    return html;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - an unordered list (ul) of the start Locations
   * - a paragraph (p) describing the destination that is reached most
   *        quickly from all of those start locations (summing travel times)
   * - a paragraph that displays the total/summed travel time that it take
   *        to reach this destination from all specified start locations
   * Or if no such destinations can be found, the HTML returned should
   * instead indicate the kind of problem encountered.
   * @param starts is the comma separated list of starting locations to
   *         search from
   * @return an HTML string that describes the closest destinations from the
   *         specified start locations.
   */
  public String generateClosestDestinationsFromAllResponseHTML(String starts) {
    List<String> startLocations = Arrays.asList(starts.split(","));
    String html = "";
    // Create unordered list (ul) of the start Locations
    html += "<ul>\n";
    for (String start : startLocations) {
      html += "  <li>" + start.trim() + "</li>\n";
    }
    html += "</ul>\n";
    // Add paragraph (p) describing the destination that is reached most quickly from all of those start locations (summing travel times)
    try {
      String closestDestination = backend.getClosestDestinationFromAll(startLocations);
      html += "<p>The destination that is reached most quickly from all of those start locations (summing travel times): \""
              + closestDestination + "\".</p>\n";
      Double total = 0.0;
      for (String start : startLocations) {
        total += this.totalTime(start.trim(), closestDestination);
      }
      html += "<p>The total/summed travel time that it take to reach this destination from all specified start locations: "
              + total + ".</p>\n";
    } catch (NoSuchElementException e) {
      html += "<p>No destination can be reached from all of the start locations, or any of the start locations does not exist within the graph.</p>\n";
    }
    return html;
  }
}
