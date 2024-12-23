package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.GKHaltNode;

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
