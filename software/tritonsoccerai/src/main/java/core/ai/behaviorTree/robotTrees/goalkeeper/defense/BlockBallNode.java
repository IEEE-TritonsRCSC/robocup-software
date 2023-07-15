package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import static proto.triton.FilteredObject.Robot;
import proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
import main.java.core.util.Vector2d;

import static main.java.core.util.ProtobufUtils.getPos;

/**
 * Moves goalkeeper to optimal position to block ball
 */
public class BlockBallNode extends TaskNode {

    private final Vector2d centerArc;
    private final int radius;
    private final MoveToPositionNode moveToPositionNode;

    public BlockBallNode(int allyID) {
        super(allyID);
        this.centerArc = new Vector2d(0, -1 * (GameInfo.getField().getFieldLength() / 2)); // arbitrary center arc
        this.radius = GameInfo.getField().getGoalWidth() / 2;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
    }

    /**
     * Gets optimal position and moves robot to that location
     */
    @Override
    public NodeState execute() {
        // System.out.println("running block ball node");
        this.moveToPositionNode.execute(findPositioningLocation());
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    public Vector2d findPositioningLocation() {
        // finds optimal vector on the arc
        float mag = centerArc.dist(new Vector2d(getPos(GameInfo.getBall())));
        Vector2d optimalLocation = new Vector2d(
                this.centerArc.x + (((GameInfo.getBall().getX() - this.centerArc.x)/mag) * radius),
                this.centerArc.y + (((GameInfo.getBall().getY() - this.centerArc.y)/mag) * radius));
        return optimalLocation; 
    }

}
