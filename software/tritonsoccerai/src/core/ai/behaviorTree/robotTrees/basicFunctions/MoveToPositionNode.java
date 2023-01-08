package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

public class MoveToPositionNode extends TaskNode {


    public MoveToPositionNode(Ally ally) {
        super("Move To Position Node: " + ally.toString(), ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(Vector2d endLoc) {
        // TODO
        // use pathfinding module to find direction to move
        // send command to move in that direction
        return NodeState.SUCCESS;
    }

}
