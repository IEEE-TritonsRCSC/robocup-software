package behaviorTree.nodes;

// leaves of tree; actionable things to do
public class TaskNode extends BTNode {

    public TaskNode() {
        super("Task Node");
    }

    public TaskNode(String name) {
        super(name);
    }

    @Override
    public boolean execute() {

    }

}
