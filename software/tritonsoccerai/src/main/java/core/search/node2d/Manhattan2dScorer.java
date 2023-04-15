package main.java.core.search.node2d;

import main.java.core.search.base.Scorer;

public class Manhattan2dScorer implements Scorer<Node2d> {
    @Override
    public float computeCost(Node2d from, Node2d to) {
        return Math.abs(to.getPos().x - from.getPos().x) + Math.abs(to.getPos().y - from.getPos().y);
    }
}
