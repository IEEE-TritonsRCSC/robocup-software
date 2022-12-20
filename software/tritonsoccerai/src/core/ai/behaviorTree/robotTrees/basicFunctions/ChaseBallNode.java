package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines task of chasing ball
 */
public class ChaseBallNode extends TaskNode {

    private final Ally ally;

    public ChaseBallNode(Ally ally) {
        super();
        this.ally = ally;
    }

    /**
     * Finds optimal path to ball and sends command to robot to move in that direction
     */
    @Override
    public NodeState execute() {
        // send command to move toward ball
        return NodeState.SUCCESS;
    }

}
