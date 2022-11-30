package core.behaviorTree.nodes.taskNodes;

import core.behaviorTree.nodes.BTNode;
import core.behaviorTree.nodes.NodeState;

// leaves of tree; actionable things to do
public class TaskNode extends BTNode {

    public TaskNode() {
        super("Task Node");
    }

    public TaskNode(String name) {
        super(name);
    }

    @Override
    public NodeState execute() {
        // TODO
        return NodeState.SUCCESS;
    }

}
