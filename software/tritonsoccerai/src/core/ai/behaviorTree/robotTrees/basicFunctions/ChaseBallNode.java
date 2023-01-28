package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import proto.triton.*;

// comment out, using robot from proto files
// import proto.triton.*;

/**
 * Defines task of chasing ball
 */
public class ChaseBallNode extends TaskNode {

    private final MoveToObjectNode moveToObjectNode;

    public ChaseBallNode(Robot ally) {
        super("Chase Ball Node: " + ally, ally);
        this.moveToObjectNode = new MoveToObjectNode(ally);
    }

    /**
     * Moves robot to move in direction of ball
     */
    @Override
    public NodeState execute() {
        this.moveToObjectNode.execute(GameInfo.getBall());
        return NodeState.SUCCESS;
    }
}
