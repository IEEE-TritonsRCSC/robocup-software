package core.ai.behaviorTree.nodes.taskNodes;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;
import core.fieldObjects.robot.Ally;

// leaves of tree; actionable things to do
/**
 * Leaves of the behavior tree
 * Define actionable things to do
 * Best practice: isolate tasks so that each node has one specific task
 */
public abstract class TaskNode extends BTNode {

    protected final Ally ally;

    public TaskNode(Ally ally) {
        super("Task Node");
        this.ally = ally;
    }

    public TaskNode(String name, Ally ally) {
        super(name);
        this.ally = ally;
    }

    /**
     * Executes task
     * Must be overriden in subclasses
     */
    @Override
    public abstract NodeState execute();

}
