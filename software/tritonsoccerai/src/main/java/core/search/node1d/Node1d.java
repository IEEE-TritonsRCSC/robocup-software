package core.search.node1d;

import core.search.base.GraphNode;

public class Node1d implements GraphNode {
    
    private final float value;

    public Node1d(float value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    
}
