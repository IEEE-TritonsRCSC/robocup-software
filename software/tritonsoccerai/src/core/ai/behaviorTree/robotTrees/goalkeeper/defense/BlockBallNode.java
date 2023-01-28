package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import proto.filtered_object.Robot;
import core.util.Vector2d;

import static core.util.ProtobufUtils.getPos;

/**
 * Moves goalkeeper to optimal position to block ball
 */
public class BlockBallNode extends TaskNode {

    private final Vector2d centerArc;
    private final int radius;
    private final MoveToPositionNode moveToPositionNode;

    public BlockBallNode(Robot ally) {
        super(ally);
        this.centerArc = new Vector2d(0, -1 * (SSL_GeometryFieldSize.field_length / 2)); // arbitrary center arc
        this.radius = SSLGeometryFieldSize.goal_width / 2;
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
        float mag = centerArc.dist(new Vector2d(getPos(GameInfo.getBall()));
        Vector2d optimalLocation = new Vector2d(
                this.centerArc.x + (((GameInfo.getBall().getX() - this.centerArc.x)/mag) * radius),
                this.centerArc.y + (((GameInfo.getBall().getY() - this.centerArc.y)/mag) * radius));
        return optimalLocation; 
    }

}
