package main.java.core.search.base;

import java.util.Map;
import java.util.Set;

public class Graph<T extends GraphNode> {

    private final Set<T> nodes;
    private final Map<T, Set<T>> connections;

    public Graph(Set<T> nodes, Map<T, Set<T>> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }    
    
    public Set<T> getConnections(T node) {
            return connections.get(node);
    }
    
}
