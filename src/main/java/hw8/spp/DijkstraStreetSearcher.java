package hw8.spp;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {

  /**
   * Creates a StreetSearcher object.
   */
  PriorityQueue<VertexDist> unexplored;
  HashMap<Vertex<String>, Double> distances;

  HashMap<Vertex<String>, Boolean> explored;

  /** Constructor for DijkstraStreetSearcher.
   *
   * @param graph the graph representing the Streets.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
    unexplored = new PriorityQueue<>();
    distances = new HashMap<>();
    explored = new HashMap<>();
  }

  @Override
  public void findShortestPath(String startName, String endName) {
    try {
      checkValidEndpoint(startName);
      checkValidEndpoint(endName);
    } catch (IllegalArgumentException ex) {
      System.out.println(ex.getMessage());
      return;
    }
    Vertex<String> start = vertices.get(startName);
    Vertex<String> end = vertices.get(endName);
    double totalDist = MAX_DISTANCE;
    populateDS(start);
    totalDist = conductSearch(totalDist, end);
    outputPath(totalDist, start, end);
  }

  private double conductSearch(double totalDist, Vertex<String> end) {
    VertexDist v;
    for (int i = 0; i < vertices.size() - 1; i++) {
      if (!unexplored.isEmpty()) {
        v = unexplored.poll();
      } else {
        break;
      }
      v = pollPQ(v);
      totalDist = updatePath(v, end, totalDist);
    }
    return totalDist;
  }


  private VertexDist pollPQ(VertexDist v) {
    while (!unexplored.isEmpty() && explored.get(v.vertex)) {
      v = unexplored.poll();
    }
    return v;
  }

  private double updatePath(VertexDist v, Vertex<String> end, double totalDist) {
    explored.put(v.vertex, true);
    for (Edge<String> edge : graph.outgoing(v.vertex)) {
      if (!explored.get(graph.to(edge))) {
        double distance = v.dist + (double) graph.label(edge);
        if (end.get().equals(graph.to(edge).get()) && distance < totalDist) {
          totalDist = distance;
        }
        if (distance < distances.get(graph.to(edge))) {
          distances.put(graph.to(edge), distance);
          graph.label(graph.to(edge), edge);
          unexplored.add(new VertexDist(graph.to(edge), distance));
        }
      }
    }
    return totalDist;
  }

  private void populateDS(Vertex<String> start) {
    for (Vertex<String> vertex : graph.vertices()) {
      if (vertex.get().equals(start.get())) {
        unexplored.add(new VertexDist(vertex, 0));
        distances.put(vertex, (double) 0);
      } else {
        unexplored.add(new VertexDist(vertex, MAX_DISTANCE));
        distances.put(vertex, MAX_DISTANCE);
      }
      explored.put(vertex, false);
    }
  }

  private void outputPath(double totalDist, Vertex<String> start, Vertex<String> end) {
    List<Edge<String>> path = getPath(end, start);
    if (VERBOSE) {
      printPath(path, totalDist);
    }
  }

  private static class VertexDist implements Comparable<VertexDist> {

    Vertex<String> vertex;

    String coords;

    double dist;

    VertexDist(Vertex<String> v, double d) {
      vertex = v;
      dist = d;
      coords = vertex.get();
    }

    @Override
    public int compareTo(VertexDist other) {
      return Double.compare(dist, other.dist);
    }

    @Override
    public int hashCode() {
      return Objects.hash(dist, vertex);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof VertexDist)) {
        return false;
      }
      VertexDist o;
      try {
        o = (VertexDist) other;
      } catch (NullPointerException | ClassCastException ex) {
        return false;
      }
      return o.dist == dist && o.coords.equals(coords);
    }
  }
}
