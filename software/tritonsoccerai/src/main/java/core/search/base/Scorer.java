package main.java.core.search.base;

public interface Scorer<T extends GraphNode> {
    float computeCost(T from, T to);
}
