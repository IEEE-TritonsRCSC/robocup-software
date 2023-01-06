package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Moves goalkeeper to optimal position to pass ball
 */
public class GKPositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public GKPositionSelfNode(Ally ally) {
        super("GKPositionSelfNode", ally);
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        this.moveToPositionNode.execute(findPositioningLocation());
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    private Vector2d findPositioningLocation() {
        // TODO
        return null;
    }
    
}
