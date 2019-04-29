/********************************************************************
 * AdjListsGraph.java @version 2019.04.11
 * Implementation of a traversable directed graph 
 * using Lists of Adjacent nodes
 *
 * KNOWN FEATURES/BUGS: 
 * It handles unweighted graphs only, but it can be extended.
 * It does not handle operations involving non-existing vertices
 ********************************************************************/

import java.util.*;
import java.io.*;

public class AdjListsGraph<T> {
    
    private final int NOT_FOUND = -1;
    
    /** Use a HashMap to make this implementation very fast! */
    private HashMap<T, LinkedList<T>> arcs;
    
    /**
     * Initiailze an empty graph.
     */
    public AdjListsGraph() {
        this.arcs = new HashMap<T, LinkedList<T>>();
    }
    
    /** Returns true if this graph is empty, false otherwise. */
    public boolean isEmpty() {
        return arcs.size() == 0;
    }
    
    /** Returns the number of vertices in this graph. */
    public int getNumVertices() { 
        return arcs.size(); 
    }
    
    /** Returns the number of arcs in this graph. */
    public int getNumArcs() {
      int totalArcs = 0;
      for (T v : arcs.keySet()) {
        totalArcs += getSuccessors(v).size();
      }
      return totalArcs; 
    }

    /** Returns all the vertices of this graph as a vector. */
    public Iterable<T> getVertices() {
      Vector<T> vertices = new Vector<T>();
      for (T v : this.arcs.keySet()) {
        vertices.add(v);
      }
      return vertices;
    }
    
    /** Returns true iff a directed edge exists between the given vertices */
    public boolean isArc(T vertex1, T vertex2){ 
        try {
            LinkedList<T> l = arcs.get(vertex1);
            return (l.indexOf(vertex2) != -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(vertex1 + " vertex isn't in the graph");
            return false;
        }
    }

    /**
     * Adds a vertex to this graph, associating object with vertex.
     * If the vertex already exists, nothing is inserted.
     */
    public void addVertex (T vertex) {
        if (!arcs.containsKey(vertex)) { 
            arcs.put(vertex, new LinkedList<T>()); 
        }
    }
    
    /**
     * Removes a single vertex with the given value from this graph.
     * If the vertex does not exist, it does not change the graph.
     */
    public void removeVertex (T vertex) {
        // int index = vertices.indexOf(vertex);
        arcs.remove(vertex);
        // this.removeVertex(index);
    }
    
    /**
     * Inserts an arc between two vertices of this graph,
     * if the vertices exist. Else it does not change the graph.
     */
    public void addArc (T source, T destination){
      if (!arcs.containsKey(source) || !arcs.containsKey(destination)) {
        return;
      }
        // int sourceIndex = vertices.indexOf(source);
        // int destinationIndex = vertices.indexOf(destination);
        
        // if source and destination exist, add the arc. do nothing otherwise
      // if ((sourceIndex != -1) && (destinationIndex != -1)){
        LinkedList<T> l = arcs.get(source);
        l.add(destination);
      // }
    }
    
    /**
     * Removes an arc between two vertices of this graph,
     * if the vertices exist. Else it does not change the graph. 
     */
    public void removeArc (T vertex1, T vertex2) {
      LinkedList<T> l = arcs.get(vertex1);
      l.remove(vertex2);
    }
    
    /**
     * Retrieve from a graph the vertices adjacent to vertex v.
     * Assumes that the vertex is in the graph.
     */
    public LinkedList<T> getSuccessors(T vertex) {
        return this.arcs.get(vertex);
    }

    /**
     * Retrieve from a graph the vertices x preceding vertex v (x->v)
     * and returns them onto a linked list. 
     * Assumes that the vertex is in the graph.
     */
    public LinkedList<T> getPredecessors(T vertex) {
        LinkedList<T> predecessors = new LinkedList<>();
        for(T v : this.arcs.keySet()) {
            if (isArc(v, vertex)) {
                predecessors.add(v);
            }
        }
        return predecessors;
    }

    /**
     * Returns a list of paths to reach all nodes reachable from the start vertex.
     * Assumes that startVertex is in this graph.
     *  
     * @param startVertex Start point for the BFS
     * @return a list of paths to all nodes reachable from startVertex
     */
    public LinkedList<LinkedList<T>> breadthFirstSearch(T startVertex) {
        LinkedList<LinkedList<T>> result = new LinkedList<LinkedList<T>>();
        
        HashSet<T> marked = new HashSet<T>();
        ArrayDeque<LinkedList<T>> queue = new ArrayDeque<LinkedList<T>>();

        LinkedList<T> startPath = new LinkedList<T>();
        startPath.add(startVertex);
        queue.add(startPath);
        marked.add(startVertex);

        while (queue.size() > 0) {
            LinkedList<T> current = queue.remove();
            result.add(current);

            for (T neighbor : getSuccessors(current.getLast())) {
                if (!marked.contains(neighbor)) {
                    LinkedList<T> newPath = new LinkedList<T>();
                    for (T elt : current) {
                        newPath.add(elt);
                    }
                    newPath.add(neighbor);
                    queue.add(newPath);
                    marked.add(neighbor);
                }
            }
        }

        return result;
    }
 
    /**
     * Returns a string representation of the graph. 
     */
    public String toString() {
        if (arcs.size() == 0) return "Graph is empty";
        StringBuilder sb = new StringBuilder();
        sb.append("# vertices in graph: " + this.getNumVertices());
        sb.append("# arcs in graph: " + this.getNumArcs());
        return sb.toString();
    }
    
    /**
     * Very Basic Driver program. 
     */
    public static void main (String args[]){
        AdjListsGraph<String> g = new AdjListsGraph<String>();
        
        System.out.println("Adding 4 vertices: A, B, C, D");
        
        g.addVertex("A"); 
        System.out.println(g.breadthFirstSearch("A"));

        g.addVertex("B"); 
        g.addVertex("C"); 
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("H");
        g.addVertex("I");
        g.addVertex("J");

        // add some edges
        g.addArc("A", "B");
        g.addArc("B", "A");
        g.addArc("A", "C");
        g.addArc("C", "B");
        g.addArc("A", "D");
        g.addArc("B", "E");
        g.addArc("E", "B");

        g.addArc("A", "F");
        g.addArc("F", "G");
        g.addArc("G", "H");
        g.addArc("A", "I");
        g.addArc("I", "J");
        g.addArc("I", "H");
        g.addArc("J", "H");

        System.out.println(g.breadthFirstSearch("A"));
    }

}
