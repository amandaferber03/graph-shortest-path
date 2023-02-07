package performance;

import hw8.Config;
import hw8.graph.Graph;
import hw8.spp.StreetSearcher;

import java.io.FileNotFoundException;

public class SystemRuntimeTest {

  private static Config config;

  public static void main(String[] args) {
    config = Config.getConfig();
    StreetSearcher.VERBOSE = false;
    System.out.println("~~~ SystemRuntimeTest ~~~");
    System.out.println(config);
    SystemRuntimeTest.loadNetwork();
    SystemRuntimeTest.findShortestPath();
    System.out.println("~~~~~~     END     ~~~~~~");
  }

  // PRE: config != null
  private static void loadNetwork() {
    Graph<String, String> graph = Config.getGraph();
    StreetSearcher streetSearcher = Config.getStreetSearcher(graph);
    try {
      long startTime, stopTime, elapsedTime;
      startTime = System.currentTimeMillis();
      streetSearcher.loadNetwork(config.data);
      stopTime = System.currentTimeMillis();
      elapsedTime = stopTime - startTime;
      System.out.printf("Loading network took %d milliseconds.\n", elapsedTime);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // PRE: config != null
  private static void findShortestPath() {
    Graph<String, String> graph = Config.getGraph();
    StreetSearcher streetSearcher = Config.getStreetSearcher(graph);
    try {
      long startTime, stopTime, elapsedTime;
      streetSearcher.loadNetwork(config.data);
      startTime = System.currentTimeMillis();
      streetSearcher.findShortestPath(config.from, config.to);
      stopTime = System.currentTimeMillis();
      elapsedTime = stopTime - startTime;
      System.out.printf("Finding shortest path took %d milliseconds.\n", elapsedTime);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
