package software.tritonsoccerai.src.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import java.core.util.Vector2d;
import static java.core.util.ProtobufUtils.getPos;

public class GKHaltNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public GKHaltNode() {
        super("GK Halt Node: " + GameInfo.getKeeper(), GameInfo.getKeeper());
        moveToPositionNode = new MoveToPositionNode(GameInfo.getKeeper());
    }

    @Override
    public NodeState execute() {
        // move to current location until moving slow enough
        float MAX_VEL_CONSTANT = 2.0f;
        while(new Vector2d(GameInfo.getKeeper().getVx(), GameInfo.getKeeper().getVy()).mag() < MAX_VEL_CONSTANT) {
            this.moveToPositionNode.execute(getPos(GameInfo.getKeeper()));
        }
        return NodeState.SUCCESS;
    }

}
