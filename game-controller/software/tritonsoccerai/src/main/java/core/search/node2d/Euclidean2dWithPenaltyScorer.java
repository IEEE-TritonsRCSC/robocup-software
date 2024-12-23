package main.java.core.search.node2d;

import main.java.core.search.base.Scorer;

public class Euclidean2dWithPenaltyScorer implements Scorer<Node2d> {
    @Override
    public float computeCost(Node2d from, Node2d to) {
        return from.getPos().dist(to.getPos()) + to.getPenalty();
    }
    
}
