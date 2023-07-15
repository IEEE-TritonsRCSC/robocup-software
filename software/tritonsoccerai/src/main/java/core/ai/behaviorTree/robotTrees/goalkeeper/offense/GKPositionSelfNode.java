package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.util.Vector2d;

import static proto.triton.FilteredObject.*;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Moves goalkeeper to optimal position to pass ball
 */
public class GKPositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    private FilteredWrapperPacket wrapper;

    public GKPositionSelfNode(FilteredWrapperPacket wrapper) {
        super("GKPositionSelfNode", 0);
        this.moveToPositionNode = new MoveToPositionNode(0);
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
