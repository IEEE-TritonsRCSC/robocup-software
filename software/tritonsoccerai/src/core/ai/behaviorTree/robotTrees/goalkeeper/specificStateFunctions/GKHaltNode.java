package software.tritonsoccerai.src.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.util.Vector2d;

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
