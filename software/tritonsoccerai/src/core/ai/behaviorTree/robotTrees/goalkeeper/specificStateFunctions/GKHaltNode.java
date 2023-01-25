package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.util.Vector2d;

public class GKHaltNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public GKHaltNode() {
        super("GK Halt Node: " + GameInfo.getKeeper().toString(), GameInfo.getKeeper());
        moveToPositionNode = new MoveToPositionNode(GameInfo.getKeeper());
    }

    @Override
    public NodeState execute() {
        // TODO send a command to stop the robot
        // No need to update the velocity
        // move to current location.
        this.moveToPositionNode.execute(new Vector2d(GameInfo.getKeeper().getX(), 
                                                    GameInfo.getKeeper().getY()));
        return NodeState.SUCCESS;
    }

}
