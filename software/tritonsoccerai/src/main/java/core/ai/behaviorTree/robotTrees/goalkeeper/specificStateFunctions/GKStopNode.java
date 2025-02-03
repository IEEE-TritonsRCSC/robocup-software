package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.GKHaltNode;

public class GKStopNode extends TaskNode {

    private final GKHaltNode haltNode;

    public GKStopNode(GKHaltNode haltNode) {
        super("GK Stop Node: " + 0, 0);
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
