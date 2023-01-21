package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;

public class GKHaltNode extends TaskNode {

    public GKHaltNode() {
        super("GK Halt Node: " + GameInfo.getKeeper().toString(), GameInfo.getKeeper());
    }

    @Override
    public NodeState execute() {
        // TODO send a command to stop the robot
        // No need to update the velocity
        return NodeState.SUCCESS;
    }

}
