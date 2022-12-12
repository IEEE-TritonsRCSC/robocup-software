package core.ai.behaviorTree.nodes.taskNodes;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;

// leaves of tree; actionable things to do
public abstract class TaskNode extends BTNode {

    public TaskNode() {
        super("Task Node");
    }

    public TaskNode(String name) {
        super(name);
    }

    @Override
    public abstract NodeState execute();

}
