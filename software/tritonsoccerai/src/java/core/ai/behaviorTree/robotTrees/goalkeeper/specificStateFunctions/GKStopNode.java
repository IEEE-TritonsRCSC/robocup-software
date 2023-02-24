package java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.GKHaltNode;

public class GKStopNode extends TaskNode {

    private final GKHaltNode haltNode;

    public GKStopNode(GKHaltNode haltNode) {
        super("GK Stop Node: " + GameInfo.getKeeper(), GameInfo.getKeeper());
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
