
 import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;


public class BackendTests {
  // tests the loadGraohData, getListOfAllLocations, findLocationsOnShortestPath
  // and getClosestDestinationFromAll methods

//   @Test
//   public void roleTest1() throws IOException {
//   DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
//   Backend backend = new Backend(graph);
//   try {
//   backend.loadGraphData("campus.dot");
//   // tests the getListOfAllLocations method
//
//   List<String> test2 = backend.getListOfAllLocations();
//   for (String test : test2) {
//   System.out.println(test);
//   }
//   List<String> expectedOutput = List.of("Union South", "Computer Sciences and Statistics",
//   "Atmospheric, Oceanic and Space Sciences", "Memorial Union");
//   Assertions.assertEquals(test2, expectedOutput, "wrong output");
//
//   // tests the findLocationsOnShortestPath method
//   List<String> test = backend.findLocationsOnShortestPath("Union South",
//   "Computer Sciences and Statistics");
//   Assertions.assertTrue(test.contains("Union South"));
//   Assertions.assertTrue(test.contains("Computer Sciences and Statistics"));
//
//   // tests the getListOfAllLocations method
//   List<String> allLocations = backend.getListOfAllLocations();
//   String destination = backend.getClosestDestinationFromAll(allLocations);
//   Assertions.assertEquals("Atmospheric, Oceanic and Space Sciences",
//   destination, "wrong");
//
//   } catch (IOException e) {
//   System.out.println(e.getMessage());
//   }
//
//   }

//   // this method tests the getListofAllLocations method
//   @Test
//   public void roleTest2() throws IOException {
//   Graph_Placeholder graph = new Graph_Placeholder();
//   Backend backend = new Backend(graph);
//
//   try {
//   backend.loadGraphData("campus.dot");
//   List<String> outputLocations = backend.getListOfAllLocations();
//   Assertions.assertTrue(graph.containsNode("Computer Sciences and Statistics"));
//   Assertions.assertTrue(graph.containsNode("Atmospheric, Oceanic and Space Sciences"));
//   Assertions.assertTrue(graph.containsNode("Union South"));
//
//   } catch (IOException e) {
//   System.out.println(e.getMessage());
//   }
//
//
//   }
//
//   // this method tests the findTimesOnShortestPath method
//   @Test
//   public void roleTest3() throws IOException {
//   Graph_Placeholder graph = new Graph_Placeholder();
//   Backend backend = new Backend(graph);
//
//   try {
//   backend.loadGraphData("campus.dot");
//   List<Double> times = backend.findTimesOnShortestPath("Union South",
//   "Computer Sciences and Statistics");
//
//   List<Double> expected = List.of(Double.valueOf("1.0"));
//   Assertions.assertEquals(expected, times, "wrong");
//
//   } catch (IOException e) {
//   System.out.println(e.getMessage());
//   }
//   }

  @Test
  public void testIntegration1()  throws IOException {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend backend = new Backend(graph);
    Frontend frontend = new Frontend(backend);
    backend.loadGraphData("campus.dot");

   System.out.println(backend.findLocationsOnShortestPath("Memorial Union", "Union South"));

   String actual = backend.findLocationsOnShortestPath("Memorial Union", "Union South").toString();
   String expected = "[Memorial Union, Radio Hall, Education Building, South Hall," +
           " Law Building, X01, Luther Memorial Church, Noland Hall, Meiklejohn House," +
           " Computer Sciences and Statistics, Union South]";
   Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testIntegration2() {

  }


}