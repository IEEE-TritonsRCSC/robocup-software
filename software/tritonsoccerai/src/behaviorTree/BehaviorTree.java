package behaviorTree;

import behaviorTree.nodes.BTNode;

public abstract class BehaviorTree {

    private final BTNode root;

    public BehaviorTree() {
        this.root = null;
    }

    private void initialize() {
        // overwrite this method
    }

    public void execute() {
        root.execute();
    }

}
