package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;
import static core.constants.ProgramConstants.aiConfig;

import static core.util.ObjectHelper.distToPath;
import static proto.triton.FilteredObject.FilteredWrapperPacket;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
/**
 * Moves goalkeeper to optimal position to pass ball
 */
public class GKPositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    private FilteredWrapperPacket wrapper;

    public GKPositionSelfNode(Ally ally, FilteredWrapperPacket wrapper) {
        super("GKPositionSelfNode", ally);
        this.moveToPositionNode = new MoveToPositionNode(ally);
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
