package core.search.base;

public class Scorer<T extends GraphNode> {
    float computeCost(T from, T to){
        return 0f;
    };
}
