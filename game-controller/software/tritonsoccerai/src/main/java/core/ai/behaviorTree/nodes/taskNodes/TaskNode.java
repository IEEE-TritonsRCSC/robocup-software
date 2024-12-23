package main.java.core.ai.behaviorTree.nodes.taskNodes;

import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import static proto.triton.FilteredObject.Robot;

// leaves of tree; actionable things to do
/**
 * Leaves of the behavior tree
 * Define actionable things to do
 * Best practice: isolate tasks so that each node has one specific task
 */
public abstract class TaskNode extends BTNode {

    protected final int allyID;

    public TaskNode(int allyID) {
        super("Task Node");
        this.allyID = allyID;
    }

    public TaskNode(String name, int allyID) {
        super(name);
        this.allyID = allyID;
    }

    /**
     * Executes task
     * Must be overriden in subclasses
     */
    @Override
    public abstract NodeState execute();

}
