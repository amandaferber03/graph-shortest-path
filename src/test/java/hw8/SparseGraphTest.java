package hw8;

import hw8.graph.Edge;
import hw8.graph.SparseGraph;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SparseGraphTest extends GraphTest {

  protected SparseGraph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }
  @Override
  protected SparseGraph<String, String> createGraph() {
    return new SparseGraph<>();
  }

  @Test
  public void test2() {
    Vertex<String> v1 = graph.insert("A");
    Vertex<String> v2 =graph.insert("B");
    Vertex<String> v3 =graph.insert("C");
    Vertex<String> v4 =graph.insert("D");
    Vertex<String> v5 =graph.insert("E");
    Vertex<String> v6 = graph.insert("G");
    graph.insert(v1, v2, null);
    graph.insert(v2, v4, null);
    graph.insert(v4, v5, null);
    graph.insert(v1, v3, null);
    graph.insert(v3, v4, null);
    System.out.println(graph.BFS("E", v1));
  }

}
