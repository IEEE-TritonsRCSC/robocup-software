package main.java.core.search.node2d;

import main.java.core.search.base.GraphNode;
import main.java.core.util.Vector2d;

public class Node2d implements GraphNode {

    private final Vector2d pos;
    private float penalty;

    public Node2d(Vector2d pos) {
        this.pos = pos;
        this.penalty = 0;
    }

    public Vector2d getPos() {
        return pos;
    }

    public float getPenalty() {
        return penalty;
    }

    public void setPenalty(float penalty) {
        this.penalty = penalty;
    }

    public void updatePenalty(float penalty) {
        if (penalty > this.penalty)
            this.penalty = penalty;
    }
    
}
