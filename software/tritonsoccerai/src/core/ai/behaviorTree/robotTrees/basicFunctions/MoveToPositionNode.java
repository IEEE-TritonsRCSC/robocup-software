package core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;
import core.search.implementation.*;
import core.search.node2d.Node2d;

public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    
    public MoveToPositionNode(Ally ally) {
        super("Move To Position Node: " + ally.toString(), ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(Vector2d endLoc) {

        Vector2d allyPos = super.ally.getPos();

        //TODO: Can't get ally ID, no ID field
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(ally.getId(), allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(actor.getId(), route);

        //TODO: Need to publish command for movement
        

        return NodeState.SUCCESS;
    }

}
