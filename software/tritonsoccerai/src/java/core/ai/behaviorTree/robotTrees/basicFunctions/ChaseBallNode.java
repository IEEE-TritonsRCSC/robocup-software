package java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import static proto.triton.FilteredObject.Robot;

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
