package behaviorTree.nodes.taskNodes;

import behaviorTree.nodes.BTNode;
import behaviorTree.nodes.NodeState;

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
