package main.java.core.search.base;

import java.util.*;

public class RouteFinder<T extends GraphNode>  {

    private final Graph<T> graph;
    private final Scorer<T> nextNodeScorer;
    private final Scorer<T> targetScorer;

    public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }

    public LinkedList<T> findRoute(T from, T to) throws IllegalStateException {
        Queue<RouteNode> openSet = new PriorityQueue<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        // add start node to open set
        RouteNode<T> start = new RouteNode<>(from, null, 0.0, targetScorer.computeCost(from, to));
        openSet.add(start);
        allNodes.put(from, start);

        while (!openSet.isEmpty()) {
            // get the node from the open set with the best score
            RouteNode<T> next = openSet.poll();

            // if we find a match, calculate the route by moving backwards
            if (next.getCurrent().equals(to))
                return walkback(allNodes, next);

            // else, do this for all the nodes connected to this node
            graph.getConnections(next.getCurrent()).forEach(connection -> {
                // get the connected node or create a new node and add it to all nodes if that connected node does not exist
                RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                allNodes.put(connection, nextNode);

                // calculate the score for the connected node by adding the distance between the next node and the connected node
                double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
                // if the new score is better, then update the connected node to point to this node and set the score of
                // the connected node to the new score
                // add the connected node to the open set for future consideration
                if (newScore < nextNode.getRouteScore()) {
                    nextNode.setPrevious(next.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                    openSet.add(nextNode);
                }
            });
        }

        throw new IllegalStateException("No route found");
    }

    private LinkedList<T> walkback(Map<T, RouteNode<T>> allNodes, RouteNode<T> to) {
        LinkedList<T> route = new LinkedList<>();
        RouteNode<T> current = to;
        do {
            route.addFirst(current.getCurrent());
            current = allNodes.get(current.getPrevious());
        } while (current != null);
        return route;
    }
    
}
