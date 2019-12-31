package misc.graphs;

import java.util.Iterator;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    
    private IDictionary<V, ChainedHashDictionary<V, E>> adjacencyList;
    //private IDictionary<> edgeWeights; // might need this, might not
    private int numVertices;
    private int numEdges;
    private IDisjointSet<V> disjointSet;
    private IList<E> edges;
    private IDictionary<V, Double> costMap = new ChainedHashDictionary<>();
    private IDictionary<V, IList<V>> pathMap = new ChainedHashDictionary<>();
    
    
    public Graph(IList<V> vertices, IList<E> edges) {
        this.edges = edges;
        numVertices = 0;
        numEdges = 0;
        disjointSet = new ArrayDisjointSet<V>();
        adjacencyList = new ChainedHashDictionary<V, ChainedHashDictionary<V, E>>();
        for (V vertex : vertices) {
            numVertices++;
            adjacencyList.put(vertex, new ChainedHashDictionary<V, E>());
            disjointSet.makeSet(vertex);
            costMap.put(vertex, Double.POSITIVE_INFINITY);
            pathMap.put(vertex, new DoubleLinkedList<V>());
        }
        for (E edge : edges) {
            if ((!adjacencyList.containsKey(edge.getVertex1())) 
                    || (!adjacencyList.containsKey(edge.getVertex2()))) {
                throw new IllegalArgumentException();
            } else if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();            
            } else {
                if (!adjacencyList.get(edge.getVertex1()).containsKey(edge.getVertex2())) {
                    adjacencyList.get(edge.getVertex1()).put(edge.getVertex2(), edge);
                }
                if (!adjacencyList.get(edge.getVertex2()).containsKey(edge.getVertex1())) {
                    adjacencyList.get(edge.getVertex2()).put(edge.getVertex1(), edge);
                }
            }
            numEdges++;
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> result = new ChainedHashSet<E>();
        IList<E> minEdgeList = Searcher.topKSort(edges.size(), edges);
        for (E edge : minEdgeList) {
            if (disjointSet.findSet(edge.getVertex1()) 
                    != disjointSet.findSet(edge.getVertex2())) {
                disjointSet.union(edge.getVertex1(), edge.getVertex2());
                result.add(edge);
            }
        }
        return result;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        for (KVPair<V, Double> pair : costMap) {
            costMap.put(pair.getKey(), Double.POSITIVE_INFINITY);
            pathMap.put(pair.getKey(), new DoubleLinkedList<V>());
        }
        // System.out.println("Start: " + start);
        // System.out.println("End: " + end);
        // System.out.println();
        IList<E> result = new DoubleLinkedList<E>();
        if (start.equals(end)) {
            return result;
        }
        //IPriorityQueue<E> unvisited = new ArrayHeap<E>();
        
        
        IList<V> unvisited = new DoubleLinkedList<>();
        
        ISet<V> visited = new ChainedHashSet<>();
        
        costMap.put(start, 0.0);
        V currV = start;
        IDictionary<V, E> curr = adjacencyList.get(start);
        unvisited.add(start);
        do {
            unvisited.delete(unvisited.indexOf(currV));
            for (KVPair<V, E> ve : curr) { // Every outbound edge
                // System.out.println();
                // System.out.println("!visited.contains(" + ve.getKey() + ") = " + !visited.contains(ve.getKey()));
                // System.out.println("costMap.get(" + ve.getKey() + ") = " + costMap.get(ve.getKey()));
                // System.out.println("costMap.get(" + currV + ") + VE.getValue().getWeight())) = " 
                //                    + (costMap.get(currV) + ve.getValue().getWeight()));
                if (!visited.contains(ve.getKey()) && (costMap.get(ve.getKey()) 
                        > (costMap.get(currV) + ve.getValue().getWeight()))) {
                    // System.out.println("Added " + currV + " to " + ve.getKey() + "'s path");
                    costMap.put(ve.getKey(), costMap.get(currV) + ve.getValue().getWeight());
                    unvisited.add(ve.getKey());
                    // System.out.println();
                    // System.out.println(ve.getKey() + " was added to unvisited");
                    IList<V> tempPath1 = pathMap.get(currV);
                    IList<V> tempPath2 = new DoubleLinkedList<V>();
                    for (int i = 0; i < tempPath1.size(); i++) {
                        tempPath2.add(tempPath1.get(i));
                    }
                    tempPath2.add(currV);
                    pathMap.put(ve.getKey(), tempPath2);
                }
            }
            visited.add(currV);
            double temp = Double.POSITIVE_INFINITY;
            //V deleteV = null;
            for (V vertex : unvisited) {
                if (costMap.get(vertex) < temp) {
                    temp = costMap.get(vertex);
                    curr = adjacencyList.get(vertex);
                    currV = vertex;
                }
            }
            //// System.out.println("indexOf(tempV) = " + unvisited.indexOf(currV));
            //// System.out.println("unvisited.size() = " + unvisited.size());
            //// System.out.println();
        } while (!unvisited.isEmpty());
        

        /*for (KVPair<V, IList<V>> pair : pathMap) {
            // System.out.println("Start to vertex: " + pair.getKey());
            for (V vertex : pair.getValue()) {
                // System.out.println(vertex);
            }
            // System.out.println();
        }*/
        /*for (V vertex : visited) {
            // System.out.println("Visited: " + vertex);
        }*/
        
        if (costMap.get(end) != Double.POSITIVE_INFINITY) {
            IList<V> vPath = pathMap.get(end);
            vPath.add(end);
            Iterator<V> iter1 = vPath.iterator();
            Iterator<V> iter2 = vPath.iterator();
            V temp1 = null;
            V temp2 = null;
            temp2 = iter2.next();
            while (iter2.hasNext()) {
                temp1 = iter1.next();
                temp2 = iter2.next();
                // System.out.println("V1 = " + temp1 + " | V2 = " + temp2);
                result.add(adjacencyList.get(temp1).get(temp2));
            }
        } else {
            throw new NoPathExistsException();
        }
        // System.out.println("result.size() = " + result.size());
        return result;
    }
}
