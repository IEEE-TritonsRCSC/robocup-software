package main.java.core.search.implementation;

import main.java.core.search.node2d.Node2d;
import main.java.core.util.Vector2d;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static proto.triton.FilteredObject.FilteredWrapperPacket;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

public class PathfindGridGroup {

    Map<Integer, PathfindGrid> pathfindGrids;

    public PathfindGridGroup(int numGrids, SSL_GeometryFieldSize field) {
        pathfindGrids = new HashMap<>();
        for (int i = 0; i < numGrids; i++)
            pathfindGrids.put(i, new PathfindGrid(field));
    }

    public void updateObstacles(FilteredWrapperPacket wrapper) {
        updateObstacles(wrapper.getAlliesMap(), wrapper.getFoesMap());
    }

    private void updateObstacles(Map<Integer, Robot> allies, Map<Integer, Robot> foes) {
        pathfindGrids.forEach((id, pathfindGrid) -> {
            Robot excludeAlly = allies.get(id);
            pathfindGrid.updateObstacles(allies, foes, excludeAlly);
        });
    }

    public Vector2d findNext(int id, Vector2d from, Vector2d to) {
        return findNext(id, findRoute(id, from, to));
    }

    public Vector2d findNext(int id, LinkedList<Node2d> route) {
        PathfindGrid pathfindGrid = pathfindGrids.get(id);
        return pathfindGrid.findNext(route);
    }

    public LinkedList<Node2d> findRoute(int id, Vector2d fromPos, Vector2d toPos) {
        PathfindGrid pathfindGrid = pathfindGrids.get(id);
        return pathfindGrid.findRoute(fromPos, toPos);
    }

    public List<Node2d> getNearestNodes(int id, Vector2d pos, float dist) {
        PathfindGrid pathfindGrid = pathfindGrids.get(id);
        return pathfindGrid.getNearestNodes(pos, dist);
    }

    public boolean checkObstacle(int id, Vector2d from, Vector2d to, double threshold) {
        PathfindGrid pathfindGrid = pathfindGrids.get(id);
        return pathfindGrid.checkPenalty(from, to, threshold);
    }

    public double getMaxPenalty(int id, Vector2d from, Vector2d to) {
        PathfindGrid pathfindGrid = pathfindGrids.get(id);
        return pathfindGrid.getMaxPenalty(from, to);
    }
    
}
