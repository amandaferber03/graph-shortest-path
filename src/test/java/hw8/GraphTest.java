package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }

  protected abstract Graph<String, String> createGraph();




  @Test
  @DisplayName("insert() successfully inserts a vertex to the graph")
  public void insertVertex() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert() successfully inserts several vertices to the graph")
  public void insertVertices() {
    ArrayList<Vertex<String>> vertices = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
     vertices.add(graph.insert("v" + i));
    }
    for (int i = 0; i < 100; i++) {
      try {
        graph.insert(vertices.get(i).get());
        fail();
      } catch (InsertionException ex) {
        return;
      }
    }
  }

  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(null) throws exception")
  public void insertNullVertex() {
    try {
      Vertex<String> v1 = graph.insert(null);
      fail("Insert() did not throw exception when null vertex was added");
    } catch (InsertionException ex) {
      System.out.println("Passed.");
    }
  }

  @Test
  @DisplayName("insert() throws exception when duplicate vertex is inserted")
  public void insertExistingVertex() {
    Vertex<String> v1 = graph.insert("v1");
    try {
      Vertex<String> v2 = graph.insert("v1");
      fail("Insert() did not throw exception when duplicate vertex was added");
    } catch (InsertionException ex) {
      System.out.println("Passed.");
    }
  }

  @Test
  @DisplayName("insert(U, V, e) successfully inserts an edge between two existing vertices")
  public void insertEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.from(e1), v1);
    assertEquals(graph.to(e1), v2);
  }

  @Test
  @DisplayName("insert() successfully inserts several edges between several existing vertices")
  public void insertSeveralEdges() {
    Vertex<String> vFrom = null;
    Vertex<String> vTo = null;
    ArrayList<Vertex<String>> vertices = new ArrayList<>();
    ArrayList<Edge<String>> edges = new ArrayList<>();
    vFrom = graph.insert("v0");
    vertices.add(vFrom);
    for (int i = 1; i < 101; i++) {
      vTo = graph.insert("v" + i);
      vertices.add(vTo);
      Edge<String> e1 = graph.insert(vFrom, vTo, "v" + (i-1) + "-v" + i);
      edges.add(e1);
      vFrom = vTo;
    }
    for (int i = 1; i < 101; i++) {
      assertEquals(vertices.get(i - 1), graph.from(edges.get(i-1)));
      assertEquals(vertices.get(i), graph.to(edges.get(i-1)));
    }
  }

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("insert(null, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenfirstVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(null, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting an edge with two invalid vertex positions")
  public void insertEdgeTwoInvalidPositions() {
    try {
      graph.insert(null, null, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting an edge that would create a self loop")
  public void insertEdgeSelfLoop() {
    Vertex<String> v = graph.insert("v");
    try {
      graph.insert(v, v, "e");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("inserting an edge that would create a duplicate edge")
  public void insertEdgeDuplicateEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    try {
      Edge<String> e2 = graph.insert(v1, v2, "v1-v2");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove() removes a vertex successfully")
  public void removeVertex() {
    Vertex<String> v1 = graph.insert("v1");
    String v1data = graph.remove(v1);
    Iterable<Vertex<String>> iterable = graph.vertices();
    Iterator<Vertex<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
    try {
      Vertex<String> vert = graph.insert("v1");
    } catch (InsertionException ex) {
      fail("The graph still contains the vertex");
    }
  }

  @Test
  @DisplayName("remove() removes several vertices (without incident edges)")
  public void removeSeveralVertices() {
    Vertex<String>[] arr = (Vertex<String>[]) Array.newInstance(Vertex.class, 100);
    for (int i = 0; i < 100; i++) {
      arr[i] = graph.insert("v" + i);
    }
    Iterable<Vertex<String>> iterable = graph.vertices();
    Iterator<Vertex<String>> itr = iterable.iterator();
    for (int i = 0; i < 100; i++) {
      String v = graph.remove(arr[i]);
      assertEquals(v, arr[i].get());
    }
  }

  @Test
  @DisplayName("remove() returns the data of the removed vertex")
  public void removeReturnsVertexData() {
    Vertex<String> v1 = graph.insert("v1");
    String v1data = graph.remove(v1);
    assertEquals(v1data, v1.get());
  }

  @Test
  @DisplayName("remove() does not remove a null vertex")
  public void removeNullVertex() {
    Vertex<String> v1 = null;
    try {
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }


  @Test
  @DisplayName("remove() does not remove a vertex with an incident edge")
  public void removeVertexWithIncidentEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    try {
      String v = graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove() successfully removes an edge")
  public void removeEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    Iterable<Edge<String>> iterable = graph.edges();
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
    try {
      Edge<String> edge = graph.insert(v1, v2, "v1-v2");
    } catch (InsertionException ex) {
      fail("The graph still contains the edge");
    }
  }

  @Test
  @DisplayName("remove() successfully removes several edges")
  public void removeSeveralEdges() {
    Edge<String>[] arr = (Edge<String>[]) Array.newInstance(Edge.class, 100);
    Vertex<String> vFrom;
    Vertex<String> vTo;
    vFrom = graph.insert("v0");
    for (int i = 1; i < 101; i++) {
      vTo = graph.insert("v" + i);
      arr[i-1] = graph.insert(vFrom, vTo, "v" + (i-1) + "-v" + i);
      vFrom = vTo;
    }

    for (int i = 1; i < 101; i++) {
      String e = graph.remove(arr[i-1]);
      assertEquals(e, arr[i-1].get());
    }
    Iterable<Edge<String>> iterable = graph.edges();
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("remove() removes a vertex only after its incident edge is removed")
  public void removeVertexAfterEdgeRemoval() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    try {
      graph.remove(v1);
    } catch (RemovalException ex) {
      fail("The vertex cannot be removed");
    }
  }

  @Test
  @DisplayName("remove() returns the data of the removed vertex")
  public void removeReturnsEdgeData() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    String e1data = graph.remove(e1);
    assertEquals(e1data, e1.get());
  }

  @Test
  @DisplayName("remove() throws exception if null edge is given")
  public void removeNullEdge() {
    Edge<String> e1 = null;
    try {
      graph.remove(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Vertices() does not iterate over anything in an empty graph")
  public void iterateOverNoVertices() {
    Iterable<Vertex<String>> iterable = graph.vertices();
    Iterator<Vertex<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Vertices() returns an iterable that can iterate over all vertices")
  public void iterateOverVertices() {
    HashSet<Vertex<String>> vertices = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      vertices.add(graph.insert("v" + i));
    }
    Iterable<Vertex<String>> iterable = graph.vertices();
    int count = 0;
    for (Vertex<String> vert : iterable) {
      assertTrue(vertices.contains(vert));
      count++;
    }
    assertEquals(count, 100);
  }

  @Test
  @DisplayName("Vertices() returns an unmodifiable iterable")
  public void iterateOverUnmodifiableVertexCollection() {
    Vertex<String>[] arr = (Vertex<String>[]) Array.newInstance(Vertex.class, 100);
    for (int i = 0; i < 100; i++) {
      arr[i] = graph.insert("v" + i);
    }
    try {
      ArrayList<Vertex<String>> vertices = (ArrayList<Vertex<String>>) graph.vertices();
      vertices.remove(arr[0]);
    } catch (UnsupportedOperationException | ClassCastException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Vertices() does not iterate over anything in an empty graph")
  public void iterateOverNoEdges() {
    Iterable<Edge<String>> iterable = graph.edges();
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Edges() returns an iterable that can iterate over all edges")
  public void iterateOverEdges() {
    HashSet<Edge<String>> edges = new HashSet<>();
    Vertex<String> vFrom;
    Vertex<String> vTo;
    vFrom = graph.insert("v0");
    for (int i = 1; i < 101; i++) {
      vTo = graph.insert("v" + i);
      edges.add(graph.insert(vFrom, vTo, "v" + (i-1) + "-v" + i));
      vFrom = vTo;
    }
    Iterable<Edge<String>> iterable = graph.edges();
    int count = 0;
    for (Edge<String> edge : iterable) {
      assertTrue(edges.contains(edge));
      count++;
    }
    assertEquals(count, 100);
  }

  @Test
  @DisplayName("Edges() returns an unmodifiable collection")
  public void edgeUnmodifiableCollection() {
    Edge<String>[] arr = (Edge<String>[]) Array.newInstance(Edge.class, 100);
    Vertex<String> vFrom;
    Vertex<String> vTo;
    vFrom = graph.insert("v0");
    for (int i = 1; i < 101; i++) {
      vTo = graph.insert("v" + i);
      arr[i-1] = graph.insert(vFrom, vTo, "v" + (i-1) + "-v" + i);
      vFrom = vTo;
    }
    try {
      ArrayList<Edge<String>> edges = (ArrayList<Edge<String>>) graph.edges();
      edges.remove(arr[0]);
    } catch (UnsupportedOperationException | ClassCastException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Outgoing() does not iterate over empty graph")
  public void outgoingWhenGraphEmpty() {
    Vertex<String> v1 = null;
    try {
      Iterable<Edge<String>> iterable = graph.outgoing(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Outgoing() does not iterate over edges if vertex has no edges")
  public void outgoingWhenNoEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Iterable<Edge<String>> iterable = graph.outgoing(v1);
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Outgoing() does not iterate over edges if vertex has no outgoing edges")
  public void outgoingWhenNoOutgoingEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v2, v1, "v2-v1");
    Iterable<Edge<String>> iterable = graph.outgoing(v1);
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Outgoing() returns an iterable that iterates over outgoing edges of vertex")
  public void iterateOverOutgoingEdges() {
    Vertex<String> v0 = graph.insert("v0");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    HashSet<Edge<String>> edges = new HashSet<>();
    edges.add(graph.insert(v0, v1, "v0-v1"));
    edges.add(graph.insert(v0, v2, "v0-v2"));
    edges.add(graph.insert(v0, v3, "v0-v3"));
    int count = 0;
    for (Edge<String> edge : graph.outgoing(v0)) {
      assertTrue(edges.contains(edge));
      count++;
    }
    assertEquals(count, 3);
  }

  @Test
  @DisplayName("Outgoing() returns an unmodifiable collection")
  public void outgoingUnmodifiableCollection() {
    Vertex<String> v0 = graph.insert("v0");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String>[] arr = (Edge<String>[]) Array.newInstance(Edge.class, 3);
    arr[0] = graph.insert(v0, v1, "v0-v1");
    arr[1] = graph.insert(v0, v2, "v0-v2");
    arr[2] = graph.insert(v0, v3, "v0-v3");
    try {
      ArrayList<Edge<String>> edges = (ArrayList<Edge<String>>) graph.outgoing(v0);
      edges.remove(arr[0]);
    } catch (UnsupportedOperationException | ClassCastException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Incoming() does not iterate over empty graph")
  public void incomingWhenGraphEmpty() {
    Vertex<String> v1 = null;
    try {
      Iterable<Edge<String>> iterable = graph.incoming(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Incoming() does not iterate over edges if vertex has no incident edges")
  public void incomingWhenNoEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Iterable<Edge<String>> iterable = graph.incoming(v1);
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Incoming() does not iterate over edges if vertex has no incoming edges")
  public void incomingWhenNoIncomingEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Iterable<Edge<String>> iterable = graph.incoming(v1);
    Iterator<Edge<String>> itr = iterable.iterator();
    assertFalse(itr.hasNext());
  }

  @Test
  @DisplayName("Incoming() returns an iterable that iterates over incoming edges of vertex")
  public void iterateOverIncomingEdges() {
    Vertex<String> v0 = graph.insert("v0");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    HashSet<Edge<String>> edges = new HashSet<>();
    edges.add(graph.insert(v1, v0, "v1-v0"));
    edges.add(graph.insert(v2, v0, "v2-v0"));
    edges.add(graph.insert(v3, v0, "v3-v0"));
    Iterable<Edge<String>> iterable = graph.incoming(v0);
    int count = 0;
    for (Edge<String> edge : iterable) {
      assertTrue(edges.contains(edge));
      count++;
    }
    assertEquals( 3, count);
  }

  @Test
  @DisplayName("Incoming() returns an unmodifiable collection")
  public void incomingUnmodifiableCollection() {
    Vertex<String> v0 = graph.insert("v0");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String>[] arr = (Edge<String>[]) Array.newInstance(Edge.class, 3);
    arr[0] = graph.insert(v1, v0, "v1-v0");
    arr[1] = graph.insert(v2, v0, "v2-v0");
    arr[2] = graph.insert(v3, v0, "v3-v0");
    try {
      ArrayList<Edge<String>> edges = (ArrayList<Edge<String>>) graph.incoming(v0);
      edges.remove(arr[0]);
    } catch (UnsupportedOperationException | ClassCastException ex) {
      return;
    }
  }

  @Test
  @DisplayName("From() successfully returns the origin vertex of an edge")
  public void getOriginVertexOfEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(v1, graph.from(e1));
    Vertex<String> v0 = graph.from(e1);
    assertEquals(v1.get(), v0.get());
  }

  @Test
  @DisplayName("From() throws exception when null edge is provided")
  public void fromWhenEdgeIsNull() {
    Edge<String> e1 = null;
    try {
      Vertex<String> v1 = graph.from(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("To() successfully returns the destination vertex of an edge")
  public void getDestinationVertexOfEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(v2, graph.to(e1));
    Vertex<String> v0 = graph.to(e1);
    assertEquals(v2.get(), v0.get());
  }

  @Test
  @DisplayName("To() throws exception when null edge is provided")
  public void toWhenEdgeIsNull() {
    Edge<String> e1 = null;
    try {
      Vertex<String> v1 = graph.to(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Label() returns null when the vertex has not been labeled")
  public void labelReturnsVertexLabel() {
    Vertex<String> v1 = graph.insert("v1");
    assertNull(graph.label(v1));
  }

  @Test
  @DisplayName("Label() throws an exception when a null vertex is given")
  public void getLabelNullVertex() {
    Vertex<String> v1 = null;
    try {
      Object v = graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Label() returns null when the edge has not been labeled")
  public void labelReturnsEdgeLabel() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("Label() throws an exception when a null edge is given")
  public void getLabelNullEdge() {
    Edge<String> e1 = null;
    try {
      Object e = graph.label(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Label() correctly relabels a vertex")
  public void labelVertex() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "vertex1");
    assertNotSame(null, graph.label(v1));
    assertEquals("vertex1", graph.label(v1));
  }

  @Test
  @DisplayName("Label() does not label a null vertex")
  public void tryLabelNullVertex() {
    Vertex<String> v1 = null;
    try {
      graph.label(v1, "vertex1");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Label() correctly relabels an edge")
  public void labelEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "edge1");
    assertNotSame(null, graph.label(e1));
    assertEquals("edge1", graph.label(e1));
  }

  @Test
  @DisplayName("Label() does not label a null edge")
  public void tryLabelNullEdge() {
    Vertex<String> v1 = null;
    try {
      graph.label(v1, "vertex1");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("All labels are null after clearLabels() is called")
  public void clearAllLabels() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.clearLabels();
    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
    assertNull(graph.label(e1));
  }

  @Test
  public void test1() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    graph.insert(v1, v2, "d");
  }

}
