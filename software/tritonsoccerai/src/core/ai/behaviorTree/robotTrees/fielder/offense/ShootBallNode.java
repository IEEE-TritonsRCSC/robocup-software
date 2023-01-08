package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.KickBallNode;
import core.constants.RobotConstants;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Shoots ball in the calculated optimal direction at max velocity
 */
public class ShootBallNode extends TaskNode {

    private final KickBallNode kickBall;

    public ShootBallNode(Ally ally) {
        super("Shoot Ball Node: " + ally.toString(), ally);
        this.kickBall = new KickBallNode(ally);
    }

    /**
     * Kicks ball as fast as possible in the optimal direction
     */
    public NodeState execute() {
        // TODO
        this.kickBall.execute(findShot(), RobotConstants.MAX_KICK_VELOCITY, false);
        return NodeState.SUCCESS;
    }

    /**
     * Finds the direction of the optimal shot
     */
    private Vector2d findShot() {
        // TODO
        return null;
    }

}
