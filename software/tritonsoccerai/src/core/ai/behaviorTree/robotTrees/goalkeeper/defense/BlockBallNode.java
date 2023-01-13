package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Moves goalkeeper to optimal position to block ball
 */
public class BlockBallNode extends TaskNode {

    private final Vector2d centerArc;
    private final int radius;
    private final MoveToPositionNode moveToPositionNode;

    public BlockBallNode(Ally ally) {
        super(ally);
        this.centerArc = new Vector2d(0, 0); // arbitrary center arc
        this.radius = 2;
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    /**
     * Gets optimal position and moves robot to that location
     */
    @Override
    public NodeState execute() {
        this.moveToPositionNode.execute(findPositioningLocation());
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    public Vector2d findPositioningLocation() {
        // finds optimal vector on the arc
        float mag = centerArc.dist(GameInfo.getBall().getPos());
        Vector2d optimalLocation = new Vector2d(
                this.centerArc.x + (((GameInfo.getBall().getX() - this.centerArc.x)/mag) * radius),
                this.centerArc.y + (((GameInfo.getBall().getY() - this.centerArc.y)/mag) * radius));
        return optimalLocation; 
    }

}
