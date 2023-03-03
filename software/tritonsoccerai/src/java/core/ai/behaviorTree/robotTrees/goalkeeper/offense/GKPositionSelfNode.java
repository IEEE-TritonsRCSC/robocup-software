package java.core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import java.core.util.Vector2d;

import static proto.triton.FilteredObject.*;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Moves goalkeeper to optimal position to pass ball
 */
public class GKPositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    private FilteredWrapperPacket wrapper;

    public GKPositionSelfNode(FilteredWrapperPacket wrapper) {
        super("GKPositionSelfNode", GameInfo.getKeeper());
        this.moveToPositionNode = new MoveToPositionNode(GameInfo.getKeeper());
        this.wrapper = wrapper;
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
        SSL_GeometryFieldSize field = this.wrapper.getField();
        float y = (-3/8)*(field.getFieldLength());
        // not sure how to get field width
        float ballXPos = GameInfo.getBall().getX();
        // if ballxPos is negative do multiply by -1 else multiple by 1
        float x = (ballXPos < 0 ? -1 : 1)*(field.getGoalWidth())/4; 
        return new Vector2d(x,y);
    }
    
}