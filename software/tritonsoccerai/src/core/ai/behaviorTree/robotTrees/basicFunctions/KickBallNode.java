package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Sends message to robot to kick ball with provided speed in provided direction
 */
public class KickBallNode extends TaskNode {

    public KickBallNode(Ally ally) {
        // TODO
        super("Kick Ball Node: " + ally.toString(), ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    /**
     * Sends message to robot to kick ball with provided speed in provided direction with/without chip
     */
    public NodeState execute(Vector2d direction, double velocity, boolean chip) {
        // TODO
        return null;
    }

}
