package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.util.Vector2d;

import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

import static proto.triton.FilteredObject.*;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Moves goalkeeper to optimal position to pass ball
 */
public class GKPositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    private final BlockBallNode blockBallNode;

    public GKPositionSelfNode() {
        super("GKPositionSelfNode", 0);
        this.moveToPositionNode = new MoveToPositionNode(0);
        this.blockBallNode = new BlockBallNode(0);
    }

    @Override
    public NodeState execute() {
        if (GameInfo.getBall().getY() > 0) {
            this.moveToPositionNode.execute(findPositioningLocation());
        }
        else {
            this.blockBallNode.execute();
        }
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    private Vector2d findPositioningLocation() {
        float y = -0.375f * GameInfo.getField().getFieldLength();
        // not sure how to get field width
        // float ballXPos = GameInfo.getBall().getX();
        // if ballxPos is negative do multiply by -1 else multiple by 1
        // float x = (ballXPos < 0 ? -1 : 1)*(GameInfo.getField().getGoalWidth())/4; 
        float x = GameInfo.getBall().getX();
        return new Vector2d(x, y);
    }
    
}
