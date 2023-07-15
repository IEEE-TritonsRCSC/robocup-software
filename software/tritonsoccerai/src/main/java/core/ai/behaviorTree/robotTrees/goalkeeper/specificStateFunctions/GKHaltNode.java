package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.util.Vector2d;
import static main.java.core.util.ProtobufUtils.getPos;

public class GKHaltNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public GKHaltNode() {
        super("GK Halt Node: " + 0, 0);
        moveToPositionNode = new MoveToPositionNode(0);
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
