package hw8.graph;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;

import java.util.*;


/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  // TODO You may need to add fields/constructor here!
  //store vertices and edges in a queue?

  HashSet<VertexNode<V>> internalVertices;
  HashSet<EdgeNode<E>> internalEdges;

  HashSet<V> vertexLabels;

  HashMap<VertexNode<V>, HashSet<VertexNode<V>>> incidences;
  boolean found;

  /** Constructor for a SparseGraph() object.
   *
   */
  public SparseGraph() {
    internalVertices = new HashSet<>();
    internalEdges = new HashSet<>();
    vertexLabels = new HashSet<>();
    incidences = new HashMap<>();
    found = false;
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  private void nullCheckV(V v) throws InsertionException {
    if (v == null) {
      throw new InsertionException();
    }
  }

  private void nullCheckV(Vertex<V> v) throws PositionException {
    if (v == null) {
      throw new PositionException();
    }
  }

  private void nullCheckE(Edge<E> e) throws PositionException {
    if (e == null) {
      throw new PositionException();
    }
  }

  //assumption that vertex was already put in convert()
  private Edge<E> createEdge(VertexNode<V> from, VertexNode<V> to, E e) {
    EdgeNode<E> edge = new EdgeNode<>(from, to, e);
    edge.owner = this;
    internalEdges.add(edge);
    addToInOutLists(edge);
    return edge;
  }

  private void removeFromVertexLists(VertexNode<V> v) {
    vertexLabels.remove(v.data);
    if (!internalVertices.remove(v)) {
      throw new PositionException();
    }
  }

  private void removeFromEdgeLists(EdgeNode<E> e) {
    if (!internalEdges.remove(e)) {
      throw new PositionException();
    }
  }

  private void addToInOutLists(EdgeNode<E> e) {
    e.from.outgoing.add(e);
    e.to.incoming.add(e);
  }

  private void removeFromInOutLists(EdgeNode<E> e) {
    e.from.outgoing.remove(e);
    e.to.incoming.remove(e);
  }

  private void checkVertices(VertexNode<V> from, VertexNode<V> to) {
    if (!(internalVertices.contains(from) && internalVertices.contains(to))) {
      throw new PositionException();
    }
  }

  public boolean BFS(V v, Vertex<V> start) {
    HashMap<Vertex<V>, Boolean> explored = new HashMap<>();
    LinkedList<VertexNode<V>> q = new LinkedList<>();
    VertexNode<V> cur = convert(start);
    for (VertexNode<V> vertex : internalVertices) {
      explored.put(vertex, false);
    }
    recBFS(explored, q, cur, v);
    boolean thing = found;
    found = false;
    return thing;
  }

  private void recBFS(HashMap<Vertex<V>, Boolean> explored, LinkedList<VertexNode<V>> q, VertexNode<V> cur, V v) {
    System.out.println(cur.get());
    if (cur.get().equals(v)) {
      found = true;
    }
    for (EdgeNode<E> edge : cur.outgoing) {
      VertexNode<V> to = edge.to;
      if (!(explored.get(to))) {
        explored.put(to, true);
        if (to.data.equals(v)) {
          found = true;
        }
        q.add(to);
      }
    }
    if (!q.isEmpty()) {
      recBFS(explored, q, q.poll(), v);
    }
  }

  public boolean DFS(V v, Vertex<V> start) {
    HashMap<Vertex<V>, Boolean> explored = new HashMap<>();
    VertexNode<V> cur = convert(start);
    for (VertexNode<V> vertex : internalVertices) {
      explored.put(vertex, false);
    }
    recDFS(explored, cur, v);
    boolean thing = found;
    found = false;
    return thing;
  }

  private void recDFS(HashMap<Vertex<V>, Boolean> explored, VertexNode<V> cur, V v) {
    if (cur.get().equals(v)) {
      found = true;
    }
    explored.put(cur, true);
    for (EdgeNode<E> edge : cur.outgoing) {
      VertexNode<V> to = edge.to;
      if (!(explored.get(to))) {
        if (to.data.equals(v)) {
          found = true;
        }
        recDFS(explored, to, v);
      }
    }
  }


  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    nullCheckV(v);
    VertexNode<V> vertex;
    if (vertexLabels.add(v)) {
      vertex = new VertexNode<>(v);
      vertex.owner = this;
    } else {
      throw new InsertionException();
    }
    internalVertices.add(vertex);
    return vertex;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e) throws PositionException, InsertionException {
    VertexNode<V> newFrom = convert(from);
    VertexNode<V> newTo = convert(to);
    if (newFrom.data.equals(newTo.data)) {
      throw new InsertionException();
    }
    if (newFrom.equals(newTo)) {
      throw new InsertionException();
    }
    checkVertices(newFrom, newTo);
    if (!incidences.containsKey(newFrom)) {
      incidences.put(newFrom, new HashSet<>());
    }
    if (!incidences.get(newFrom).add(newTo)) {
      throw new InsertionException();
    }
    return createEdge(newFrom, newTo, e);
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    nullCheckV(v);
    VertexNode<V> vertex = convert(v);
    if (vertex.incoming.isEmpty() && vertex.outgoing.isEmpty()) {
      removeFromVertexLists(vertex);
      incidences.remove(vertex);
    } else {
      throw new RemovalException();
    }
    return vertex.data;
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    nullCheckE(e);
    EdgeNode<E> edge = convert(e);
    incidences.get(edge.from).remove(edge.to);
    removeFromInOutLists(edge);
    removeFromEdgeLists(edge);
    return edge.data;
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return Collections.unmodifiableCollection(internalVertices);
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return Collections.unmodifiableCollection(internalEdges);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    return Collections.unmodifiableCollection(convert(v).outgoing);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    return Collections.unmodifiableCollection(convert(v).incoming);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    return convert(e).from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    return convert(e).to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    convert(v).label = l;
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    convert(e).label = l;
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    return convert(v).label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    return convert(e).label;
  }

  @Override
  public void clearLabels() {
    for (VertexNode<V> vertex : internalVertices) {
      vertex.label = null;
    }
    for (EdgeNode<E> edge : internalEdges) {
      edge.label = null;
    }
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;

    ArrayList<EdgeNode<E>> incoming;
    ArrayList<EdgeNode<E>> outgoing;

    VertexNode(V v) {
      this.data = v;
      this.label = null;
      this.incoming = new ArrayList<>();
      this.outgoing = new ArrayList<>();
    }

    @Override
    public boolean equals(Object v) {
      if (v == this) {
        return true;
      }
      if (!(v instanceof VertexNode)) {
        return false;
      }
      VertexNode<V> vertex = (VertexNode<V>) v;
      return data.equals(vertex.data) && owner == vertex.owner && label.equals(vertex.label);
    }

    @Override
    public int hashCode() {
      return Objects.hash(data, owner);
    }

    @Override
    public V get() {
      return this.data;
    }
  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;
    // TODO You may need to add fields/methods here!

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
    }
    /*
    @Override
    public boolean equals(Object e) {
      if (this == e) {
        return true;
      }
      if (e == null) {
        return false;
      }
      if (!(e instanceof EdgeNode)) {
        return false;
      }
      EdgeNode<E> edge;
      try {
        edge = (EdgeNode<E>) e;
      } catch (NullPointerException | ClassCastException ex) {
        return false;
      }
      boolean edgeLabel = labelCheck(edge);
      boolean direction = (from == edge.from && to == edge.to);
      return data.equals(edge.data) && owner.equals(edge.owner) && direction && edgeLabel;
    }

    private boolean labelCheck(EdgeNode<E> edge) {
      boolean edgeLabel;
      if (label == null) {
        edgeLabel = edge.label == null;
      } else {
        if (edge.label != null) {
          edgeLabel = label.equals(edge.label);
        } else {
          return false;
        }
      }
      return edgeLabel;
    }

    @Override
    public int hashCode() {
      return Objects.hash(data, owner);
    }

   */

    @Override
    public E get() {
      return this.data;
    }
  }
}
