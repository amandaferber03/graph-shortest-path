package performance;

import hw8.Config;
import hw8.graph.Graph;
import hw8.spp.StreetSearcher;

import java.io.FileNotFoundException;

public class MemoryMonitorTest {

  private static final int KB = 1024;
  private static Config config;
  private static double usedMemory = 0;

  public static void main(String[] args) {
    config = Config.getConfig();
    StreetSearcher.VERBOSE = false;
    System.out.println("~~~ MemoryMonitorTest ~~~");
    System.out.println(config);
    MemoryMonitorTest.loadNetworkAndFindShortestPath();
    System.out.println("~~~~~~     END     ~~~~~~");
  }

  // PRE: config != null
  private static void loadNetworkAndFindShortestPath() {
    reportUsedMemory();
    System.out.println("Instantiating empty Graph data structure");
    Graph<String, String> graph = Config.getGraph();
    System.out.println("Instantiating empty StreetSearcher object");
    StreetSearcher streetSearcher = Config.getStreetSearcher(graph);
    reportUsedMemory();
    try {
      System.out.println("Loading the network");
      streetSearcher.loadNetwork(config.data);
      reportUsedMemory();
      System.out.println("Finding the shortest path");
      streetSearcher.findShortestPath(config.from, config.to);
      reportUsedMemory();
      streetSearcher = null;
      graph = null;
      System.out.println("Setting objects to null (so GC does its thing!)");
      reportUsedMemory();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void reportUsedMemory() {
    double last = usedMemory;
    usedMemory = (double) MemoryMonitor.getReallyUsedMemory() / KB;
    if (last == 0) {
      last = usedMemory;
    }
    double delta = usedMemory - last;
    System.out.printf("\tUsed memory: %.2f KB (Δ = %.3f)\n", usedMemory, delta);
  }
}
