package core.ai.behaviorTree.nodes.taskNodes;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;

// leaves of tree; actionable things to do
/**
 * Leaves of the behavior tree
 * Define actionable things to do
 * Best practice: isolate tasks so that each node has one specific task
 */
public abstract class TaskNode extends BTNode {

    public TaskNode() {
        super("Task Node");
    }

    public TaskNode(String name) {
        super(name);
    }

    /**
     * Executes task
     * Must be overriden in subclasses
     */
    @Override
    public abstract NodeState execute();

}
